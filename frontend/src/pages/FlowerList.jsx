import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { listFlowers } from '../api.js';

export default function FlowerList() {
  const [flowers, setFlowers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [name, setName] = useState('');
  const [color, setColor] = useState('');

  useEffect(() => {
    const handle = setTimeout(() => {
      setLoading(true);
      listFlowers({ name, color })
        .then(data => {
          setFlowers(data);
          setError(null);
        })
        .catch(err => setError(err.message))
        .finally(() => setLoading(false));
    }, 200);
    return () => clearTimeout(handle);
  }, [name, color]);

  return (
    <>
      <div className="search">
        <input
          type="text"
          placeholder="Search by name"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Filter by color"
          value={color}
          onChange={e => setColor(e.target.value)}
        />
      </div>

      {loading && <div className="loading">Loading…</div>}
      {error && <div className="error">{error}</div>}
      {!loading && !error && flowers.length === 0 && (
        <div className="empty">No flowers found. <Link to="/flowers/new">Add the first one</Link>.</div>
      )}

      <div className="grid">
        {flowers.map(f => (
          <Link key={f.id} to={`/flowers/${f.id}`} className="card">
            <h3>{f.name}</h3>
            <div className="color">{f.color || '—'}</div>
            <div className="price">${f.price?.toFixed(2)}</div>
          </Link>
        ))}
      </div>
    </>
  );
}