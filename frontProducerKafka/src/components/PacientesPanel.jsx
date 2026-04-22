import { useState } from "react";

const BASE = "http://localhost:8000/producer";

export default function PacientesPanel() {
  const [nombre, setNombre] = useState("");
  const [id, setId] = useState("");
  const [estado, setEstado] = useState("");

  const enviar = async (endpoint, body) => {
    const res = await fetch(`${BASE}${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    if (res.ok) setEstado("✅ Enviado a Kafka");
    else setEstado("❌ Error al enviar");
    setTimeout(() => setEstado(""), 3000);
  };

  return (
    <div className="panel">
      <h2>👥 Gestión de Pacientes</h2>
      {estado && <p className="status">{estado}</p>}
      <div className="form-group">
        <input placeholder="Nombre del paciente" value={nombre} onChange={e => setNombre(e.target.value)} />
        <input placeholder="ID del paciente" value={id} onChange={e => setId(e.target.value)} />
      </div>
      <div className="btn-group">
        <button className="btn-green" onClick={() => enviar("/pacientes/registro", `Registrar paciente: ${nombre}, ID: ${id}`)}>
          ➕ Registrar (Partición 0)
        </button>
        <button className="btn-blue" onClick={() => enviar("/pacientes/actualizacion", `Actualizar paciente ID: ${id}, nombre: ${nombre}`)}>
          ✏️ Actualizar (Partición 1)
        </button>
        <button className="btn-red" onClick={() => enviar("/pacientes/eliminacion", `Eliminar paciente ID: ${id}`)}>
          🗑️ Eliminar (Partición 2)
        </button>
      </div>
    </div>
  );
}