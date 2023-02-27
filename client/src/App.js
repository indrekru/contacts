import './App.css';

import CryptoJS from 'crypto-js';
import React, { useState } from 'react';

export default function App() {

    const api = 'http://localhost:8080/api/v1/contacts';
    const keyBase64 = 'YXlydGllb3d5dllydHczNGl1SHl0aHZuZWl0dXdFaU8=';
    const [secret, setSecret] = useState(keyBase64)
    const [data, setData] = useState([]);
    const [newContact, setNewContact] = useState(null);
    const [query, setQuery] = useState('');

    function loadContacts() {
        setNewContact(null);
        setQuery('');
        fetch(api)
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw response;
        })
        .then(response => {
            setData(response);
        });
    }

    function createNewContact() {
        setData([]);
        setQuery('');
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
        var name = encryptWord(newContact.name);
        var codeName = encryptWord(newContact.codeName);
        var phone = encryptWord(newContact.phone);

        setNewContact({
             name: name,
             codeName: codeName,
             phone: phone,
             encrypted: true
       });
    }

    function encryptWord(word){
        var key = CryptoJS.enc.Base64.parse(secret);
        var srcs = CryptoJS.enc.Utf8.parse(word);
        var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        return encrypted.toString();
    }

    function decryptWord(word){
      var key = CryptoJS.enc.Base64.parse(secret);
      var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
      return CryptoJS.enc.Utf8.stringify(decrypt).toString();
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

    function doSearch(e) {
      var newValue = e.target.value;
      setQuery(newValue);

      setNewContact(null);
      fetch(api + '/search?q=' + newValue)
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw response;
      })
      .then(response => {
        setData(response);
      });
    }

    function decrypt(item) {
        var id = item.id;
        data.find(x => x.id === id).name = decryptWord(data.find(x => x.id === id).name);
        data.find(x => x.id === id).codeName = decryptWord(data.find(x => x.id === id).codeName);
        data.find(x => x.id === id).phone = decryptWord(data.find(x => x.id === id).phone);
        data.find(x => x.id === id).decrypted = true;
        setData(data.map(item => item));
    }

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Kontaktid
        </p>
        <div>
          <button onClick={loadContacts}>
              Lae kontaktid
          </button>
          <br></br>
          <button onClick={createNewContact}>
              Uus kontakt
          </button>
          <br></br>
          <input type="text" placeholder="Search..." value={query} onChange={doSearch}/>
        </div>
        {/* List */}
        {data && data.map((item) =>
          <div key={item.id}>
            <div>-----------------------------</div>
            {!item.decrypted && <button onClick={(e) => decrypt(item)}>Decrypt</button>}
            <div>Item Id: {item.id}</div>
            <div>Name: {item.name}</div>
            <div>Code name: {item.codeName}</div>
            <div>Phone: {item.phone}</div>
          </div>
        )}
        {/* List end */}

        {/* New contact */}
        {newContact &&
            <div>
                <h3>Create new contact</h3>
                <br></br>
                <label>Name: </label>
                <input id="name" type="text" value={newContact.name} onChange={handleChange} />
                <br></br>
                <label>Code name: </label>
                <input id="codeName" type="text" value={newContact.codeName} onChange={handleChange} />
                <br></br>
                <label>Phone: </label>
                <input id="phone" type="text" value={newContact.phone} onChange={handleChange} />
                <br></br>
                {!newContact.encrypted && <button onClick={encrypt}>Encrypt</button>}
                {newContact.encrypted && <button onClick={postContact}>Save to database</button>}
            </div>
        }
        {/* New contact end */}
      </header>
    </div>
  );
};
