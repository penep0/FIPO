import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function StockDetailPage() {
  const BASE_URL = import.meta.env.VITE_API_URL;

  const { isinCd } = useParams();
  const navigate = useNavigate();
  const [stock, setStock] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [portfolios, setPortfolios] = useState([]);
  const [selectedPortfolioId, setSelectedPortfolioId] = useState('');
  const [quantity, setQuantity] = useState('');

  useEffect(() => {
    setIsLoading(true);
    fetch(`${BASE_URL}/api/stock/search/isinCd?isinCd=${isinCd}`)
      .then((res) => {
        if (!res.ok) throw new Error('주식 정보를 불러오지 못했습니다.');
        return res.json();
      })
      .then((data) => setStock(data))
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));

    const token = localStorage.getItem('accessToken');
    if (token) {
      fetch(`${BASE_URL}/api/portfolio/list`, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        credentials: 'include'
      })
        .then((res) => res.json())
        .then((data) => setPortfolios(data))
        .catch((err) => console.error('포트폴리오 목록 불러오기 실패:', err));
    }
  }, [isinCd]);

  const handleAddToPortfolio = () => {
    const token = localStorage.getItem('accessToken');
    if (!token) return alert('로그인이 필요합니다.');
    if (!selectedPortfolioId) return alert('포트폴리오를 선택해주세요.');
    if (!quantity || isNaN(quantity) || quantity <= 0) return alert('올바른 수량을 입력해주세요.');

    fetch(`${BASE_URL}/api/portfolio/add?portfolioId=${selectedPortfolioId}&isinCd=${stock.isinCd}&quantity=${quantity}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      credentials: 'include',
      body: JSON.stringify({
        isinCd: stock.isinCd,
        portfolioId: selectedPortfolioId,
        quantity: parseInt(quantity)
      })
    })
      .then((res) => {
        if (!res.ok) throw new Error('포트폴리오 추가 실패');
        return res.text();
      })
      .then(() => {
        alert('포트폴리오에 추가되었습니다!');
      })
      .catch((err) => {
        console.error(err);
        alert('추가에 실패했습니다.');
      });
  };

  if (isLoading) {
    return <div className="text-center text-gray-400 mt-10">📦 로딩 중...</div>;
  }

  if (error || !stock) {
    return (
      <div className="text-center text-red-400 mt-10">
        ❗ 오류: {error || '데이터 없음'}
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-zinc-900 text-white px-6 py-10">
      <div className="max-w-4xl mx-auto">
        <button
          onClick={() => navigate(-1)}
          className="mb-8 px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-600"
        >
          ← 뒤로가기
        </button>

        <div className="bg-zinc-800 p-6 rounded-lg shadow-lg mb-6">
          <h1 className="text-3xl font-bold mb-2">{stock.itmsNm}</h1>
          <p className="text-gray-400 mb-4">ISIN 코드: {stock.isinCd}</p>

          <div className="grid grid-cols-2 gap-6 text-base">
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">📈 현재가</span>
              <div className="text-xl font-semibold">{stock.clpr.toLocaleString()}원</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">📊 등락률</span>
              <div className="text-xl font-semibold">{stock.fltRt}%</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">💹 대비</span>
              <div className="text-xl font-semibold">{stock.vs}</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">🏁 시가</span>
              <div className="text-xl font-semibold">{stock.mkp.toLocaleString()}원</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">📈 고가</span>
              <div className="text-xl font-semibold">{stock.hipr.toLocaleString()}원</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">📉 저가</span>
              <div className="text-xl font-semibold">{stock.lopr.toLocaleString()}원</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">📊 거래량</span>
              <div className="text-xl font-semibold">{stock.trqu.toLocaleString()}주</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">💰 거래대금</span>
              <div className="text-xl font-semibold">{stock.trPrc.toLocaleString()}원</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">🧾 상장주식수</span>
              <div className="text-xl font-semibold">{stock.lstgStCnt.toLocaleString()}</div>
            </div>
            <div className="bg-zinc-700 p-4 rounded">
              <span className="text-sm text-gray-400">🏦 시가총액</span>
              <div className="text-xl font-semibold">{stock.mrktTotAmt.toLocaleString()}원</div>
            </div>
          </div>

          <div className="mt-10 bg-zinc-700 p-6 rounded text-sm">
            <h2 className="text-lg font-semibold mb-4">📥 포트폴리오에 추가</h2>

            <div className="flex flex-col md:flex-row gap-4 items-center">
              <select
                value={selectedPortfolioId}
                onChange={(e) => setSelectedPortfolioId(e.target.value)}
                className="bg-zinc-800 border border-zinc-600 rounded px-4 py-2 text-white"
              >
                <option value="">포트폴리오 선택</option>
                {portfolios.map((p) => (
                  <option key={p.id} value={p.id}>{p.portfolioName}</option>
                ))}
              </select>
              <input
                type="number"
                value={quantity}
                onChange={(e) => setQuantity(e.target.value)}
                placeholder="매수 수량"
                className="bg-zinc-800 border border-zinc-600 rounded px-4 py-2 text-white w-36"
              />
              <button
                onClick={handleAddToPortfolio}
                className="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2 rounded"
              >
                ➕ 추가하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default StockDetailPage;
