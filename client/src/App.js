import './App.css';

import SimpleCrypto from "simple-crypto-js"
import React, { useState, useEffect } from 'react';

export default function App() {

    const [secret, setSecret] = useState('secret')
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [newContact, setNewContact] = useState(false);

    function loadContacts() {
        setLoading(true);
        setNewContact(false);
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

    function createNewContact() {
        setData([]);
        setNewContact(true);
    }

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Kontaktid (key: {secret})
        </p>
        <button onClick={loadContacts}>
          Lae kontaktid
        </button>
        <button onClick={createNewContact}>
            Uus kontakt
        </button>
        {/* List */}
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
        {/* List end */}

        {/* New contact */}
        {newContact &&
            <div>
                <label>Name:</label>
                <input type="text" />
                <label>Code name:</label>
                <input type="text" />
                <label>Phone:</label>
                <input type="text" />
            </div>
        }
        {/* New contact end */}
      </header>
    </div>
  );
};
