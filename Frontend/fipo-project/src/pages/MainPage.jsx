// MainPage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import MarketSelector from './components/MarketSelector';
import MyPageButton from './components/MyPageButton';
import SearchBar from './components/SearchBar';
import SortOptions from './components/SortOptions';
import StockTable from './components/StockTable';
import Pagination from './components/Pagination';

function MainPage() {
  const token = localStorage.getItem('token');
  const navigate = useNavigate();
  const [stocks, setStocks] = useState([]);
  const [market, setMarket] = useState('kospi');
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [search, setSearch] = useState('');
  const [searchedStock, setSearchedStock] = useState(null);
  const [sort, setSort] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    fetch(`http://localhost:8080/api/stock/${market}?page=${page}`,{
        headers: {
          'Authorization': `Bearer ${token}`
        }
    })
      .then((res) => res.json())
      .then((data) => {
        setStocks(data);
        setHasNext(data.length > 0);
      })
      .catch((err) => {
        console.error('데이터 로딩 실패:', err);
        setStocks([]);
        setHasNext(false);
      })
      .finally(() => setIsLoading(false));
  }, [market, page]);

  const handleSort = (type) => setSort(type);
  const handleRowClick = (isinCd) => navigate(`/stock/${isinCd}`);

  const filtered = stocks.filter((stock) =>
    stock.itmsNm.toLowerCase().includes(search.toLowerCase())
  );

  const handleMarketChange = (newMarket) => {
    setMarket(newMarket);
    setPage(1); // 마켓이 바뀌면 페이지를 1로 초기화
  };

  const sorted = [...filtered];
  if (sort === 'price') {
    sorted.sort((a, b) => b.clpr - a.clpr);
  } else if (sort === 'rate') {
    sorted.sort((a, b) => b.fltRt - a.fltRt);
  }

// 검색어로 백엔드 검색
const handleSearch = () => {
    if (search.trim() === '') {
      setSearchedStock(null);
      return;
    }
  
    setIsLoading(true);
    fetch(`http://localhost:8080/api/stock/search/itmsNm?itmsNm=${encodeURIComponent(search)}`)
      .then((res) => {
        if (!res.ok) throw new Error("검색 실패");
        return res.json();
      })
      .then((data) => setSearchedStock(data))
      .catch((err) => {
        console.error(err);
        setSearchedStock(null);
      })
      .finally(() => setIsLoading(false));
  };

  return (
    <div className="min-h-screen bg-zinc-900 text-white p-8">
      <div className="flex justify-between items-center mb-6">
        <MarketSelector market={market} setMarket={handleMarketChange} />
        <MyPageButton onClick={() => navigate('/myPage')} />
      </div>

      <SearchBar search={search} setSearch={setSearch} onSearch={handleSearch} />
      <SortOptions handleSort={handleSort} />
      <StockTable stocks={searchedStock ? [searchedStock] : sorted} onRowClick={handleRowClick} isLoading={isLoading} />
      <Pagination page={page} setPage={setPage} hasNext={hasNext} />
    </div>
  );
}

export default MainPage;
