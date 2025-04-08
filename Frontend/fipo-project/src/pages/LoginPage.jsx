import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
  const navigate = useNavigate();

  const BASE_URL = import.meta.env.VITE_API_URL;

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await fetch("${BASE_URL}/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("이메일 또는 비밀번호가 잘못되었습니다.");
      }

      const data = await response.json();
      localStorage.setItem("token", data.token); // ✅ JWT 저장
      navigate("/dashboard"); // ✅ 로그인 성공 시 이동
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSocialLogin = (provider) => {
    window.location.href = `${BASE_URL}/oauth2/authorization/${provider}`;
  };

  return (
    <div className="min-h-screen bg-zinc-900 flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-zinc-800 p-8 rounded-2xl shadow-xl">
        <h2 className="text-white text-3xl font-bold text-center mb-8">로그인</h2>

        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label className="block text-sm text-gray-300 mb-1">이메일</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-2 bg-zinc-700 text-white rounded-md border border-zinc-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition"
            />
          </div>
          <div>
            <label className="block text-sm text-gray-300 mb-1">비밀번호</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-2 bg-zinc-700 text-white rounded-md border border-zinc-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition"
            />
          </div>

          {error && (
            <p className="text-sm text-red-400">{error}</p>
          )}

          <button
            type="submit"
            disabled={loading}
            className={`w-full py-2 text-white rounded-md font-medium transition ${
              loading ? 'bg-indigo-300 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700'
            }`}
          >
            {loading ? '로그인 중...' : '로그인'}
          </button>
        </form>

        <div className="mt-8 space-y-3">
          <button
            onClick={() => handleSocialLogin('google')}
            className="w-full py-2 rounded-md bg-white text-gray-800 font-semibold hover:bg-gray-200 transition"
          >
            Google로 로그인
          </button>
          <button
            onClick={() => handleSocialLogin('kakao')}
            className="w-full py-2 rounded-md bg-yellow-300 text-gray-900 font-semibold hover:bg-yellow-400 transition"
          >
            Kakao로 로그인
          </button>
          <button
            onClick={() => handleSocialLogin('naver')}
            className="w-full py-2 rounded-md bg-green-500 text-white font-semibold hover:bg-green-600 transition"
          >
            Naver로 로그인
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;