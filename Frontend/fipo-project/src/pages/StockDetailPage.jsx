// StockDetailPage.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function StockDetailPage() {
  const { isinCd } = useParams();
  const navigate = useNavigate();
  const [stock, setStock] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setIsLoading(true);
    fetch(`http://localhost:8080/stock/api/search/isinCd?isinCd=${isinCd}`)
      .then((res) => {
        if (!res.ok) throw new Error('주식 정보를 불러오지 못했습니다.');
        return res.json();
      })
      .then((data) => setStock(data))
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
  }, [isinCd]);

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
    <div className="min-h-screen bg-zinc-900 text-white p-8">
      <button
        onClick={() => navigate(-1)}
        className="mb-6 px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-600"
      >
        ← 뒤로가기
      </button>

      <h1 className="text-3xl font-bold mb-4">{stock.itmsNm}</h1>
      <p className="text-gray-400 mb-6">ISIN 코드: {stock.isinCd}</p>

      <div className="grid grid-cols-2 gap-4 text-lg">
        <div>📈 현재가: {stock.clpr.toLocaleString()}원</div>
        <div>📊 등락률: {stock.fltRt}%</div>
        <div>💹 대비: {stock.vs}</div>
        <div>🏁 시가: {stock.mkp}</div>
        <div>📈 고가: {stock.hipr}</div>
        <div>📉 저가: {stock.lopr}</div>
        <div>📊 거래량: {stock.trqu.toLocaleString()}</div>
        <div>💰 거래대금: {stock.trPrc.toLocaleString()}</div>
        <div>🧾 상장주식수: {stock.lstgStCnt.toLocaleString()}</div>
        <div>🏦 시가총액: {stock.mrktTotAmt.toLocaleString()}원</div>
      </div>
    </div>
  );
}

export default StockDetailPage;