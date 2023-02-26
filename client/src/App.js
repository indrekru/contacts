import logo from './logo.svg';
import './App.css';

import React, { useState, useEffect } from 'react';

export default function App() {

    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);

    function loadContacts() {
        setLoading(true);
        fetch('http://localhost:8080/api/v1/contacts')
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw response;
        })
        .then(response => {
            setData(response);
        })
        .finally(() => setLoading(false));
    }

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Kontaktid
        </p>
        <button onClick={loadContacts}>
          Lae kontaktid
        </button>
        {loading && <div>Loading...</div>}
        {data && data.map((item) =>
          <div key={item.id}>
            <div>Item Id: {item.id}</div>
            <div>Name: {item.name}</div>
            <div>Code name: {item.codeName}</div>
            <div>Phone: {item.phone}</div>
            <div>-----------------------------</div>
          </div>
        )}
      </header>
    </div>
  );
};
