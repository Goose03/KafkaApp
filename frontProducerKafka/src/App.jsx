import PacientesPanel from "./components/PacientesPanel";
import CitasPanel from "./components/CitasPanel";
import VisualizacionPanel from "./components/VisualizacionPanel";
import "./App.css";

function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1>🏥 Sistema para Doctores</h1>
        <p>Gestión de pacientes y citas médicas</p>
      </header>
      <main className="app-main">
        <PacientesPanel />
        <CitasPanel />
        <VisualizacionPanel />
      </main>
    </div>
  );
}

export default App;