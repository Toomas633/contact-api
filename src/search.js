import React, { useState } from "react";
import "./search.css";

function Search() {
  const [results, setResults] = useState([]);

  const handleFormSubmit = (event) => {
    event.preventDefault();
    const searchInput = document.getElementById("search-input").value;
    if (!(searchInput.length < 1)) {
      let data = {
        value: searchInput,
      };
      const xhr = new XMLHttpRequest();
      xhr.open("POST", "http://localhost:8000/search");
      xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (Array.isArray(response) && response.length === 0) {
              setResults([{ id: -1, nimi: "", salajane: "", tel: "" }]);
            } else {
              setResults(response);
            }
          } else {
            console.error("Error:", xhr.status);
          }
        }
      };
      xhr.send(JSON.stringify(data));
    }
  };

  const handleResultClick = (id) => {
    window.location.href = `/contact/${id}`;
  };

  const handleAddButtonClick = () => {
    window.location.href = "/add";
  };

  return (
    <div className="search">
      <div className="search-bar">
      <button className="add" onClick={handleAddButtonClick}>
            Lisa
          </button>
        <form onSubmit={handleFormSubmit}>
          <input id="search-input" type="text" placeholder="Sisesta..." />
          <input type="submit" value="&#x1F50D;" />
        </form>
      </div>
      <div id="results-container">
        {results.map((item) =>
          item.id !== -1 ? (
            <div
              key={item.id}
              className="result-item"
              onClick={() => handleResultClick(item.id)}
            >
              <p>Nimi: {item.nimi}</p>
              <p>Salajane: {item.salajane}</p>
              <p>Tel: {item.tel}</p>
            </div>
          ) : (
            <div key={item.id} className="result-item">
              <p>Vasteid ei leitud</p>
            </div>
          )
        )}
      </div>
    </div>
  );
}

export default Search;
