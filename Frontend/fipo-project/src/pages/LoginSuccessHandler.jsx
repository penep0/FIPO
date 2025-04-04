import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

function LoginSuccessHandler() {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const query = new URLSearchParams(location.search);
    const accessToken = query.get("accessToken");
    const refreshToken = query.get("refreshToken");

    if (accessToken && refreshToken) {
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      navigate("/"); // 또는 navigate('/mypage');
    } else {
      navigate("/login");
    }
  }, [location, navigate]);

  return <div className="text-center text-white mt-20">🔄 로그인 처리 중입니다...</div>;
}

export default LoginSuccessHandler;