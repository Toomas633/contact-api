import React, { useState } from 'react';

function App() {
  const [results, setResults] = useState([]);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    const searchInput = document.getElementById("search-input").value;
    if (!(searchInput.length < 1)) {
      const url = "http://localhost:8000/search";
      fetch(url, {
        method: "POST",
        body: "search:" + searchInput,
      })
        .then((response) => response.json())
        .then((data) => {
          if (Array.isArray(data) && data.length === 0) {
            setResults([{ id: -1, nimi: 'Vasteid ei leitud.', salajane: '', tel: '' }]);
          } else {
            setResults(data);
          }
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  };

  const handleResultClick = (id) => {
    window.location.href = `http://localhost:3000/contact/${id}`;
  };

  return (
    <div className="App">
      <div className="search-bar">
        <form onSubmit={handleFormSubmit}>
          <input id="search-input" type="text" placeholder="Sisesta..." />
          <input type="submit" value="Otsi" />
        </form>
      </div>
      <div id="results-container">
        {results.map((item) => (
          <div
            key={item.id}
            className="result-item"
            onClick={() => handleResultClick(item.id)}
          >
            <p>Nimi: {item.nimi}</p>
            <p>Salajane: {item.salajane}</p>
            <p>Tel: {item.tel}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;
