// Pagination.jsx
import React from 'react';

function Pagination({ page, setPage, hasNext }) {
  return (
    <div className="flex justify-center space-x-4">
      <button
        onClick={() => setPage((prev) => Math.max(prev - 1, 1))}
        disabled={page === 1}
        className="px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-600 disabled:opacity-50"
      >
        이전
      </button>
      <span className="text-gray-300">페이지 {page}</span>
      <button
        onClick={() => setPage((prev) => (hasNext ? prev + 1 : prev))}
        disabled={!hasNext}
        className="px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-600 disabled:opacity-50"
      >
        다음
      </button>
    </div>
  );
}

export default Pagination;