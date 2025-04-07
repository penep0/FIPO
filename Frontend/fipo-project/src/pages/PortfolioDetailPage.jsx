import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function PortfolioDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [portfolio, setPortfolio] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) return navigate('/login');

    fetch(`http://localhost:8080/api/portfolio/info/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      credentials: 'include'
    })
      .then(res => {
        if (!res.ok) throw new Error('정보 조회 실패');
        return res.json();
      })
      .then(data => setPortfolio(data))
      .catch(err => {
        console.error(err);
        setError('포트폴리오 정보를 불러오는 데 실패했습니다.');
      })
      .finally(() => setIsLoading(false));
  }, [id]);

  if (isLoading) return <div className="text-center text-gray-400 mt-10">📦 로딩 중...</div>;
  if (error) return <div className="text-center text-red-400 mt-10">❗ {error}</div>;
  if (!portfolio) return null;

  return (
    <div className="min-h-screen bg-zinc-900 text-white p-8">
      <h1 className="text-3xl font-bold mb-6">📊 포트폴리오 상세</h1>

      <div className="bg-zinc-800 p-6 rounded mb-6">
        <p><strong>이름:</strong> {portfolio.portfolioName}</p>
        <p><strong>총 자산:</strong> {portfolio.totalCash?.toLocaleString?.() ?? 0}원</p>
        <p><strong>수익률:</strong> {portfolio.earningRate ?? 0}%</p>
        <p><strong>수익금:</strong> {portfolio.proceeds?.toLocaleString?.() ?? 0}원</p>
      </div>

      {portfolio.stocks && portfolio.stocks.length > 0 ? (
        <div className="mt-6 bg-zinc-800 p-6 rounded">
          <h2 className="text-2xl font-semibold mb-4">📈 보유 종목</h2>
          <table className="w-full text-left text-sm">
            <thead className="border-b border-zinc-600 text-gray-300">
              <tr>
                <th className="py-2">종목명</th>
                <th className="py-2">보유 수량</th>
                <th className="py-2">투자 금액</th>
                <th className="py-2">시장 구분</th>
                <th className="py-2">시가</th>
                <th className="py-2">수익률</th>
                <th className="py-2">수익금</th>
                <th className="py-2">행동</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.stocks.map((stock, index) => (
                <tr key={index} className="border-b border-zinc-700">
                  <td className="py-2">{stock.itmsNm}</td>
                  <td className="py-2">{stock.stockNum.toLocaleString()}주</td>
                  <td className="py-2">{stock.cash.toLocaleString()}원</td>
                  <td className="py-2">{stock.mrktCtg}</td>
                  <td className="py-2">{stock.mkp.toLocaleString()}원</td>
                  <td className="py-2">{stock.proceeds.toFixed(2)}%</td>
                  <td className="py-2">{stock.earningMoney.toLocaleString()}원</td>
                  <td className="py-2 space-x-2">
                    <button className="px-2 py-1 bg-green-600 rounded hover:bg-green-700 text-sm">매수</button>
                    <button className="px-2 py-1 bg-red-600 rounded hover:bg-red-700 text-sm">매도</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="text-gray-400 mt-4">보유 중인 종목이 없습니다.</p>
      )}

      <div className="bg-zinc-800 p-6 rounded mt-6">
        <button
          className="px-4 py-2 bg-indigo-600 rounded hover:bg-indigo-700"
          onClick={() => navigate(-1)}
        >
          🔙 돌아가기
        </button>
      </div>
    </div>
  );
}

export default PortfolioDetailPage;
