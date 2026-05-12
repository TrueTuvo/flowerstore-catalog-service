import { Link, Route, Routes } from 'react-router-dom';
import FlowerList from './pages/FlowerList.jsx';
import FlowerDetail from './pages/FlowerDetail.jsx';
import FlowerForm from './pages/FlowerForm.jsx';

export default function App() {
  return (
    <div className="app">
      <header className="header">
        <Link to="/" className="brand">Flower Catalog</Link>
        <nav>
          <Link to="/flowers/new" className="btn btn-primary">+ New flower</Link>
        </nav>
      </header>
      <main className="main">
        <Routes>
          <Route path="/" element={<FlowerList />} />
          <Route path="/flowers/new" element={<FlowerForm mode="create" />} />
          <Route path="/flowers/:id" element={<FlowerDetail />} />
          <Route path="/flowers/:id/edit" element={<FlowerForm mode="edit" />} />
          <Route path="*" element={<p>Not found.</p>} />
        </Routes>
      </main>
    </div>
  );
}