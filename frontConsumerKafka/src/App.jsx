import { useState, useEffect } from "react";
import "./App.css";

const BASE = "http://localhost:8100/consumer";
const TOPICOS = ["gestion-pacientes", "gestion-citas", "visualizacion-estado"];

const ETIQUETAS = {
  "gestion-pacientes": "👥 Gestión de Pacientes",
  "gestion-citas": "📅 Gestión de Citas",
  "visualizacion-estado": "👁️ Visualización / Estado",
};

export default function App() {
  const [mensajes, setMensajes] = useState([]);
  const [topicoActivo, setTopicoActivo] = useState("todos");
  const [idBusqueda, setIdBusqueda] = useState("");

  // Consultar endpoint del consumer cada 2 segundos
  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const url = topicoActivo === "todos"
          ? `${BASE}/mensajes`
          : `${BASE}/mensajes/${topicoActivo}`;
        const res = await fetch(url);
        const data = await res.json();
        setMensajes(data);
      } catch (e) {
        console.error("Error consultando consumer:", e);
      }
    }, 2000);
    return () => clearInterval(interval);
  }, [topicoActivo]);

  const mensajesFiltrados = idBusqueda
    ? mensajes.filter(m => m.contenido?.includes(idBusqueda))
    : mensajes;

  return (
    <div className="app">
      <header className="app-header">
        <h1>🖥️ Sistema de Visualización</h1>
        <p>Consulta de citas y estado de pacientes en tiempo real</p>
      </header>

      <div className="controles">
        <div className="tabs">
          <button className={topicoActivo === "todos" ? "tab active" : "tab"} onClick={() => setTopicoActivo("todos")}>
            Todos
          </button>
          {TOPICOS.map(t => (
            <button key={t} className={topicoActivo === t ? "tab active" : "tab"} onClick={() => setTopicoActivo(t)}>
              {ETIQUETAS[t]}
            </button>
          ))}
        </div>
        <input
          className="buscador"
          placeholder="🔍 Buscar por ID de paciente..."
          value={idBusqueda}
          onChange={e => setIdBusqueda(e.target.value)}
        />
      </div>

      <main className="mensajes-lista">
        {mensajesFiltrados.length === 0 ? (
          <p className="vacio">No hay mensajes aún. Los mensajes aparecerán aquí cuando el consumer los reciba de Kafka.</p>
        ) : (
          mensajesFiltrados.map((m, i) => (
            <div key={i} className={`mensaje-card topico-${m.topico}`}>
              <div className="mensaje-header">
                <span className="badge">{ETIQUETAS[m.topico] || m.topico}</span>
                <span className="badge-particion">Partición {m.particion} — {m.tipo}</span>
                <span className="hora">{m.hora}</span>
              </div>
              <p className="mensaje-contenido">{m.contenido}</p>
            </div>
          ))
        )}
      </main>
    </div>
  );
}