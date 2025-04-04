// StockTable.jsx
import React from 'react';

function StockTable({ stocks, onRowClick, isLoading }) {
  if (isLoading) {
    return (
      <div className="text-center py-8 text-gray-400 text-lg">📦 로딩 중입니다...</div>
    );
  }

  return (
    <table className="w-full text-left border-collapse mb-6">
      <thead>
        <tr className="border-b border-gray-700">
          <th className="pb-2">종목명</th>
          <th className="pb-2">현재가</th>
          <th className="pb-2">등락률</th>
        </tr>
      </thead>
      <tbody>
        {stocks.map((stock, index) => (
          <tr
            key={index}
            onClick={() => onRowClick(stock.isinCd)}
            className="border-b border-gray-800 hover:bg-zinc-800 cursor-pointer"
          >
            <td className="py-2">{stock.itmsNm}</td>
            <td className="py-2">{stock.clpr.toLocaleString()}원</td>
            <td
              className={`py-2 ${
                stock.fltRt > 0 ? 'text-green-400' : stock.fltRt < 0 ? 'text-red-400' : 'text-white'
              }`}
            >
              {stock.fltRt}%
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default StockTable;