import { useState } from "react";

const BASE = "http://localhost:8000/producer";

export default function CitasPanel() {
  const [pacienteId, setPacienteId] = useState("");
  const [fecha, setFecha] = useState("");
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
      <h2>📅 Gestión de Citas</h2>
      {estado && <p className="status">{estado}</p>}
      <div className="form-group">
        <input placeholder="ID del paciente" value={pacienteId} onChange={e => setPacienteId(e.target.value)} />
        <input type="date" value={fecha} onChange={e => setFecha(e.target.value)} />
      </div>
      <div className="btn-group">
        <button className="btn-green" onClick={() => enviar("/citas/creacion", `Crear cita para paciente ID: ${pacienteId}, fecha: ${fecha}`)}>
          ➕ Crear cita (Partición 0)
        </button>
        <button className="btn-red" onClick={() => enviar("/citas/cancelacion", `Cancelar cita de paciente ID: ${pacienteId}`)}>
          ❌ Cancelar cita (Partición 1)
        </button>
        <button className="btn-blue" onClick={() => enviar("/citas/reprogramacion", `Reprogramar cita de paciente ID: ${pacienteId}, nueva fecha: ${fecha}`)}>
          🔄 Reprogramar (Partición 2)
        </button>
      </div>
    </div>
  );
}