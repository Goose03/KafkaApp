import { useState } from "react";

const BASE = "http://localhost:8000/producer";

export default function VisualizacionPanel() {
  const [pacienteId, setPacienteId] = useState("");
  const [estado, setEstado] = useState("");

  const enviar = async (endpoint, body) => {
    try {
      const res = await fetch(`${BASE}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
      if (res.ok) setEstado("✅ Enviado a Kafka correctamente");
      else setEstado("❌ Error al enviar");
    } catch (e) {
      setEstado("❌ No se pudo conectar con el servidor");
    }
    setTimeout(() => setEstado(""), 3000);
  };

  return (
    <div className="panel">
      <h2>👁️ Visualización / Estado</h2>
      <p className="subtitulo">Tópico: <code>visualizacion-estado</code></p>
      {estado && <p className="status">{estado}</p>}
      <div className="form-group">
        <input
          placeholder="ID del paciente"
          value={pacienteId}
          onChange={(e) => setPacienteId(e.target.value)}
        />
      </div>
      <div className="btn-group">
        <button
          className="btn-blue"
          onClick={() =>
            enviar(
              "/visualizacion/estado",
              `Consultar estado del paciente ID: ${pacienteId}`
            )
          }
        >
          👁️ Consultar estado
          <span className="particion-tag">Partición 0</span>
        </button>
        <button
          className="btn-purple"
          onClick={() =>
            enviar(
              "/visualizacion/historial",
              `Consultar historial médico del paciente ID: ${pacienteId}`
            )
          }
        >
          📂 Consultar historial médico
          <span className="particion-tag">Partición 1</span>
        </button>
      </div>
    </div>
  );
}