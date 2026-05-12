import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { createFlower, getFlower, updateFlower } from '../api.js';

export default function FlowerForm({ mode }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const editing = mode === 'edit';

  const [form, setForm] = useState({ name: '', color: '', price: '' });
  const [loading, setLoading] = useState(editing);
  const [submitting, setSubmitting] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!editing) return;
    setLoading(true);
    getFlower(id)
      .then(f => setForm({ name: f.name ?? '', color: f.color ?? '', price: f.price ?? '' }))
      .catch(err => setError(err.status === 404 ? 'Flower not found.' : err.message))
      .finally(() => setLoading(false));
  }, [id, editing]);

  function handleChange(field) {
    return e => {
      setForm(prev => ({ ...prev, [field]: e.target.value }));
      setFieldErrors(prev => ({ ...prev, [field]: undefined }));
    };
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setFieldErrors({});
    setError(null);
    const payload = {
      name: form.name,
      color: form.color || null,
      price: form.price === '' ? null : Number(form.price),
    };
    try {
      const saved = editing
        ? await updateFlower(id, payload)
        : await createFlower(payload);
      navigate(`/flowers/${saved.id}`);
    } catch (err) {
      if (err.status === 400 && err.fieldErrors) {
        setFieldErrors(err.fieldErrors);
      } else {
        setError(err.message);
      }
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) return <div className="loading">Loading…</div>;

  return (
    <>
      <Link to={editing ? `/flowers/${id}` : '/'} className="back-link">← Back</Link>
      <form className="form" onSubmit={handleSubmit}>
        <h1>{editing ? 'Edit flower' : 'New flower'}</h1>

        <div className="form-field">
          <label htmlFor="name">Name</label>
          <input id="name" type="text" value={form.name} onChange={handleChange('name')} />
          {fieldErrors.name && <div className="field-error">{fieldErrors.name}</div>}
        </div>

        <div className="form-field">
          <label htmlFor="color">Color</label>
          <input id="color" type="text" value={form.color} onChange={handleChange('color')} />
          {fieldErrors.color && <div className="field-error">{fieldErrors.color}</div>}
        </div>

        <div className="form-field">
          <label htmlFor="price">Price</label>
          <input
            id="price"
            type="number"
            step="0.01"
            min="0"
            value={form.price}
            onChange={handleChange('price')}
          />
          {fieldErrors.price && <div className="field-error">{fieldErrors.price}</div>}
        </div>

        {error && <div className="error">{error}</div>}

        <div className="actions">
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Saving…' : 'Save'}
          </button>
          <Link to={editing ? `/flowers/${id}` : '/'} className="btn">Cancel</Link>
        </div>
      </form>
    </>
  );
}