// MarketSelector.jsx
import React from 'react';

function MarketSelector({ market, setMarket }) {
  return (
    <div className="flex space-x-2">
      <button
        onClick={() => setMarket('kospi')}
        className={`px-4 py-2 rounded ${
          market === 'kospi' ? 'bg-indigo-600' : 'bg-zinc-700'
        }`}
      >
        KOSPI
      </button>
      <button
        onClick={() => setMarket('kosdaq')}
        className={`px-4 py-2 rounded ${
          market === 'kosdaq' ? 'bg-indigo-600' : 'bg-zinc-700'
        }`}
      >
        KOSDAQ
      </button>
    </div>
  );
}

export default MarketSelector;