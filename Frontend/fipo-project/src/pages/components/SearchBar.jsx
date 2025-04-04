// SearchBar.jsx
import React from 'react';

function SearchBar({ search, setSearch, onSearch }) {
  return (
    <div className="flex space-x-2 mb-4">
      <input
        type="text"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="종목명 검색"
        className="w-full px-4 py-2 rounded bg-zinc-800 border border-zinc-600 text-white"
      />
      <button
        onClick={onSearch}
        className="px-4 py-2 bg-indigo-600 rounded hover:bg-indigo-700"
      >
        검색
      </button>
    </div>
  );
}

export default SearchBar;