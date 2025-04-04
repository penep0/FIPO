// MyPageButton.jsx
import React from 'react';

function MyPageButton({ onClick }) {
  return (
    <button
      onClick={onClick}
      className="px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-600"
    >
      마이페이지
    </button>
  );
}

export default MyPageButton;