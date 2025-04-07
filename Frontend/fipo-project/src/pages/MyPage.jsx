// MyPage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function MyPage() {
  const token = localStorage.getItem('accessToken');
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [portfolios, setPortfolios] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newPortfolioName, setNewPortfolioName] = useState('');
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    if (!token) {
      navigate('/login'); // 🔒 로그인 안 되어 있으면 redirect
      return;
    }

    loadData(token);
  }, []);

  const loadData = async (accessToken) => {
    setIsLoading(true);
    try {
      const userRes = await fetch('http://localhost:8080/api/user/load', {
        headers: { Authorization: `Bearer ${accessToken}` },
        credentials: 'include'
      });
  
      // 🔁 Access Token 만료 시 refresh 시도
      if (userRes.status === 401) {
        const refreshRes = await fetch('http://localhost:8080/api/auth/refresh', {
          method: 'POST',
          credentials: 'include'
        });
  
        if (!refreshRes.ok) throw new Error('리프레시 토큰도 만료됨');
  
        const refreshData = await refreshRes.json();
        const newAccessToken = refreshData.accessToken;
        localStorage.setItem('accessToken', newAccessToken);
  
        // 🔁 재시도
        return loadData(newAccessToken);
      }
  
      const portfolioRes = await fetch('http://localhost:8080/api/portfolio/list', {
        headers: { Authorization: `Bearer ${accessToken}` },
        credentials: 'include'
      });
  
      if (!portfolioRes.ok) throw new Error('포트폴리오 로딩 실패');
  
      const userData = await userRes.json();
      const portfolioData = await portfolioRes.json();
  
      setUser(userData);
      setPortfolios(portfolioData);
    } catch (err) {
      console.error("🔥 에러 발생:", err.message);
      console.error("전체 에러 객체:", err);
      setError('정보를 불러오는 데 실패했습니다.');
      navigate('/login');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreatePortfolio = () => {
    const token = localStorage.getItem('accessToken');
    if (!newPortfolioName.trim()) return;
    setCreating(true);
    fetch('http://localhost:8080/api/portfolio/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ name: newPortfolioName }),
      credentials: 'include' // 👈 반드시 있어야 쿠키 같이 감
    })
      .then((res) => {
        if (!res.ok) throw new Error('생성 실패');
        return res.json();
      })
      .then(() => {
        setNewPortfolioName('');
        loadData(token); // ✅ 새로고침 시 토큰 유지
      })
      .catch((err) => {
        console.error(err);
        alert('포트폴리오 생성에 실패했습니다.');
      })
      .finally(() => setCreating(false));
  };

  const handleDeletePortfolio = (portfolioId) => {
    const token = localStorage.getItem('accessToken');
    if (!window.confirm('정말 삭제하시겠습니까?')) return;
  
    fetch(`http://localhost:8080/api/portfolio/delete/${portfolioId}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`
      },
      credentials: 'include'
    })
      .then((res) => {
        if (!res.ok) throw new Error('삭제 실패');
        return res.json(); // 백엔드가 JSON 반환할 경우
      })
      .then(() => {
        loadData(token); // 삭제 후 목록 갱신
      })
      .catch((err) => {
        console.error(err);
        alert('포트폴리오 삭제에 실패했습니다.');
      });
  };

  if (isLoading) return <div className="text-center text-gray-400 mt-10">📦 로딩 중...</div>;
  if (error) return <div className="text-center text-red-400 mt-10">❗ {error}</div>;

  return (
    <div className="min-h-screen bg-zinc-900 text-white p-8">
      <h1 className="text-3xl font-bold mb-6">👤 마이페이지</h1>

      <div className="mb-8 bg-zinc-800 p-4 rounded">
        <p><strong>닉네임:</strong> {user.userName}</p>
        <p><strong>보유 현금:</strong> {user.money.toLocaleString()}원</p>
      </div>

      <div className="mb-6">
        <h2 className="text-2xl font-semibold mb-2">➕ 새 포트폴리오</h2>
        <div className="flex space-x-2">
          <input
            type="text"
            value={newPortfolioName}
            onChange={(e) => setNewPortfolioName(e.target.value)}
            placeholder="포트폴리오 이름 입력"
            className="px-4 py-2 rounded bg-zinc-800 border border-zinc-600 text-white w-full"
          />
          <button
            onClick={handleCreatePortfolio}
            disabled={creating}
            className="px-4 py-2 bg-indigo-600 rounded hover:bg-indigo-700 disabled:opacity-50"
          >
            생성
          </button>
        </div>
      </div>

      <div className="mb-6">
        <h2 className="text-2xl font-semibold mb-2">💼 내 포트폴리오</h2>
        {portfolios.length === 0 ? (
          <p className="text-gray-400">포트폴리오가 없습니다.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {portfolios.map((portfolio) => (
              <div key={portfolio.id} className="bg-zinc-800 p-4 rounded shadow">
                <h3 className="text-xl font-bold mb-2">{portfolio.portfolioName}</h3>
                <p>총 자산: {portfolio.totalCash}원</p>
                <p>종목 수: {portfolio.stocks?.length ?? 0}</p>
                <div className="flex justify-end mt-2 space-x-2">
                  <button 
                    onClick={() => navigate(`/portfolio/${portfolio.id}`)}
                    className="px-3 py-1 bg-zinc-700 rounded hover:bg-zinc-600">
                      🔍 보기
                  </button>
                  <button 
                    onClick={() => handleDeletePortfolio(portfolio.id)}
                    disabled={creating}
                    className="px-3 py-1 bg-red-600 rounded hover:bg-red-700">
                    🗑️ 삭제
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyPage;