import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { deleteFlower, getFlower } from '../api.js';

export default function FlowerDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [flower, setFlower] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    getFlower(id)
      .then(setFlower)
      .catch(err => setError(err.status === 404 ? 'Flower not found.' : err.message))
      .finally(() => setLoading(false));
  }, [id]);

  async function handleDelete() {
    if (!confirm(`Delete "${flower.name}"?`)) return;
    try {
      await deleteFlower(id);
      navigate('/');
    } catch (err) {
      setError(err.message);
    }
  }

  if (loading) return <div className="loading">Loading…</div>;
  if (error) return (
    <>
      <Link to="/" className="back-link">← Back to catalog</Link>
      <div className="error">{error}</div>
    </>
  );

  return (
    <>
      <Link to="/" className="back-link">← Back to catalog</Link>
      <div className="detail">
        <h1>{flower.name}</h1>
        <div className="row"><span className="label">Color</span><span>{flower.color || '—'}</span></div>
        <div className="row"><span className="label">Price</span><span>${flower.price?.toFixed(2)}</span></div>
        <div className="row"><span className="label">ID</span><span>{flower.id}</span></div>
        <div className="actions">
          <Link to={`/flowers/${flower.id}/edit`} className="btn btn-primary">Edit</Link>
          <button type="button" className="btn btn-danger" onClick={handleDelete}>Delete</button>
        </div>
      </div>
    </>
  );
}