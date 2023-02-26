import './App.css';

import SimpleCrypto from "simple-crypto-js"
import React, { useState, useEffect } from 'react';

export default function App() {

    const api = 'http://localhost:8080/api/v1/contacts';
    const [secret, setSecret] = useState('secretsecretsecret')
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [newContact, setNewContact] = useState(null);
    const encryptor = new SimpleCrypto(secret);

    function loadContacts() {
        setLoading(true);
        setNewContact(null);
        fetch(api)
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
        setNewContact({
            name: '',
            codeName: '',
            phone: '',
            encrypted: false
        });
    }

    function handleChange(e) {
        var field = e.target.id;
        var newValue = e.target.value;
        newContact[field] = newValue;
        setNewContact({
            name: newContact.name,
            codeName: newContact.codeName,
            phone: newContact.phone,
            encrypted: newContact.encrypted
       });
    }

    function encrypt() {
        var name = newContact.name;
        var codeName = encryptor.encrypt(newContact.codeName);
        var phone = encryptor.encrypt(newContact.phone);

        setNewContact({
             name: name,
             codeName: codeName,
             phone: phone,
             encrypted: true
       });
    }

    function postContact() {
        fetch(api, {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(newContact)
        }).finally(() => {
            loadContacts();
        });
    }

    function decrypt(item) {
        var id = item.id;
        data.find(x => x.id === id).codeName = encryptor.decrypt(data.find(x => x.id === id).codeName);
        data.find(x => x.id === id).phone = encryptor.decrypt(data.find(x => x.id === id).phone);
        data.find(x => x.id === id).decrypted = true;
        setData(data.map(item => item));
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
            {!item.decrypted && <button onClick={(e) => decrypt(item)}>Decrypt</button>}
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
                <br></br>
                <label>Name:</label>
                <input id="name" type="text" value={newContact.name} onChange={handleChange} />
                <br></br>
                <label>Code name:</label>
                <input id="codeName" type="text" value={newContact.codeName} onChange={handleChange} />
                <br></br>
                <label>Phone:</label>
                <input id="phone" type="text" value={newContact.phone} onChange={handleChange} />
                <br></br>
                {!newContact.encrypted && <button onClick={encrypt}>Encrypt</button>}
                {newContact.encrypted && <button onClick={postContact}>Create contact</button>}
            </div>
        }
        {/* New contact end */}
      </header>
    </div>
  );
};
