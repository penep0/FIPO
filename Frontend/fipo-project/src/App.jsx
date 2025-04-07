import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import StockDetailPage from './pages/StockDetailPage';
import MyPage from './pages/MyPage';
import LoginSuccessHandler from './pages/LoginSuccessHandler';
import PortfolioDetailPage from './pages/PortfolioDetailPage';

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<MainPage />} />
      <Route path="/stock/:isinCd" element={<StockDetailPage />} />
      <Route path="/login/success" element={<LoginSuccessHandler />} />
      <Route path="/myPage" element={<MyPage />} />
      <Route path="/portfolio/:id" element={<PortfolioDetailPage />} />
    </Routes>
  );
}

export default App;