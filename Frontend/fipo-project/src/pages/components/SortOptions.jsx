// SortOptions.jsx
import React from 'react';

function SortOptions({ handleSort }) {
  return (
    <div className="flex space-x-2 mb-4">
      <button onClick={() => handleSort('price')} className="px-3 py-1 bg-zinc-700 rounded">
        현재가순
      </button>
      <button onClick={() => handleSort('rate')} className="px-3 py-1 bg-zinc-700 rounded">
        등락률순
      </button>
    </div>
  );
}

export default SortOptions;