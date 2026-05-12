const BASE = 'http://localhost:8081/api/flowers';

async function request(url, options = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });

  if (res.status === 204) return null;

  const text = await res.text();
  const body = text ? JSON.parse(text) : null;

  if (!res.ok) {
    const message = typeof body === 'object' && body !== null
      ? Object.entries(body).map(([k, v]) => `${k}: ${v}`).join(', ')
      : `Request failed with ${res.status}`;
    const err = new Error(message);
    err.status = res.status;
    err.fieldErrors = typeof body === 'object' ? body : {};
    throw err;
  }

  return body;
}

export function listFlowers({ name = '', color = '' } = {}) {
  const params = new URLSearchParams();
  if (name) params.set('name', name);
  if (color) params.set('color', color);
  const qs = params.toString();
  return request(qs ? `${BASE}?${qs}` : BASE);
}

export function getFlower(id) {
  return request(`${BASE}/${id}`);
}

export function createFlower(payload) {
  return request(BASE, { method: 'POST', body: JSON.stringify(payload) });
}

export function updateFlower(id, payload) {
  return request(`${BASE}/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
}

export function deleteFlower(id) {
  return request(`${BASE}/${id}`, { method: 'DELETE' });
}