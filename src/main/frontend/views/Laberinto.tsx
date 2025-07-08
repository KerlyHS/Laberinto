import React, { useState } from 'react';

const Laberinto: React.FC = () => {
  const [dimension, setDimension] = useState<number | ''>('');
  const [error, setError] = useState('');
  const [mostrarBotonResolver, setMostrarBotonResolver] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [laberinto, setLaberinto] = useState<number[][]>([]);

  const manejarCambio = (e: React.ChangeEvent<HTMLInputElement>) => {
    const valor = parseInt(e.target.value);
    if (!isNaN(valor)) {
      setDimension(valor);
      if (valor < 30 || valor > 100) {
        setError('Ingrese un valor entre 30 y 100');
        setMostrarBotonResolver(false);
        setLaberinto([]);
      } else {
        setError('');
        setMostrarBotonResolver(true);
        generarLaberinto(valor);
      }
    } else {
      setDimension('');
      setError('Solo se permiten números');
      setMostrarBotonResolver(false);
      setLaberinto([]);
    }
  };

  const generarLaberinto = (dim: number) => {
    const nuevoLaberinto: number[][] = [];
    for (let i = 0; i < dim; i++) {
      const fila: number[] = [];
      for (let j = 0; j < dim; j++) {
        fila.push(Math.random() < 0.3 ? 1 : 0); // 30% muros, 70% camino
      }
      nuevoLaberinto.push(fila);
    }
    // Asegura entrada y salida
    nuevoLaberinto[0][0] = 0;
    nuevoLaberinto[dim - 1][dim - 1] = 0;
    setLaberinto(nuevoLaberinto);
  };

  const resolverLaberinto = (desea: boolean) => {
    if (desea) {
      setMensaje(`(Simulación) Resolviendo laberinto de dimensión ${dimension}x${dimension}...`);
      // Aquí podrías llamar a tu backend real si tienes uno
    } else {
      setMensaje('Saliendo sin resolver el laberinto.');
    }
  };

  return (
    <div style={{ padding: '2rem', background: '#111', color: '#fff', fontFamily: 'Arial' }}>
      <h2>Generador de Laberinto</h2>
      <label>Ingrese la dimensión del laberinto (min. 30, max. 100):</label>
      <br />
      <input
        type="number"
        value={dimension}
        onChange={manejarCambio}
        style={{ padding: '0.5rem', marginTop: '0.5rem' }}
      />
      <br />
      {error && <span style={{ color: 'red' }}>{error}</span>}

      {laberinto.length > 0 && (
        <div style={{ marginTop: '1rem', display: 'grid', gridTemplateColumns: `repeat(${dimension}, 10px)` }}>
          {laberinto.flat().map((celda, index) => (
            <div
              key={index}
              style={{
                width: 10,
                height: 10,
                backgroundColor: celda === 1 ? '#444' : '#0f0',
                border: '1px solid #222',
              }}
            ></div>
          ))}
        </div>
      )}

      {mostrarBotonResolver && (
        <div style={{ marginTop: '1rem' }}>
          <p>¿Desea resolver el laberinto?</p>
          <button onClick={() => resolverLaberinto(true)} style={{ marginRight: '1rem' }}>Sí</button>
          <button onClick={() => resolverLaberinto(false)}>No</button>
        </div>
      )}

      {mensaje && <p style={{ marginTop: '1rem' }}>{mensaje}</p>}
    </div>
  );
};

export default Laberinto;
