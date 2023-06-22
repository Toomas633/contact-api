import React, { useState } from "react";
import "./add.css";

function Add() {
  const [Nimi, setInput1] = useState("");
  const [Salajane, setInput2] = useState("");
  const [Tel, setInput3] = useState("");
  const [popupMessage, setPopupMessage] = useState("");

  const handleInputChange1 = (event) => {
    setInput1(event.target.value);
  };

  const handleInputChange2 = (event) => {
    setInput2(event.target.value);
  };

  const handleInputChange3 = (event) => {
    setInput3(event.target.value);
  };

  const handleSaveClick = () => {
    if (!(Nimi < 1 || Salajane < 1 || Tel < 1)) {
      const data = {
        nimi: Nimi,
        salajane: Salajane,
        tel: Tel,
      };
      const xhr = new XMLHttpRequest();
      xhr.open("POST", "http://localhost:8000/add");
      xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
      xhr.send(JSON.stringify(data));

      xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            setPopupMessage("Kontakt lisatud");
            setTimeout(function () {
              window.location.href = "/";
            }, 3000);
          } else {
            const errorMessage = xhr.responseText || "Error";
            setPopupMessage(errorMessage);
          }
        }
      };
    } else {
      setPopupMessage("Täida kõik väljad");
    }
  };
  const handleBackClick = () => {
    window.location.href = "/";
  };

  return (
    <div className="lisa">
      <div className="navbar">
        <button className="back" onClick={handleBackClick}>
          Tagasi
        </button>
      </div>
      <div className="väljad">
        <div className="sisend">
          <p>Nimi</p>
          <input type="text" value={Nimi} onChange={handleInputChange1} />
        </div>
        <div className="sisend">
          <p>Koodnimi</p>
          <input type="text" value={Salajane} onChange={handleInputChange2} />
        </div>
        <div className="sisend">
          <p>Tel</p>
          <input type="text" value={Tel} onChange={handleInputChange3} />
        </div>
        <div className="sisend">
          <button className="save" onClick={handleSaveClick}>
            Salvesta
          </button>
          {popupMessage && (
            <div
              className={`popup ${
                popupMessage === "Kontakt lisatud" ? "success" : "error"
              }`}
            >
              {popupMessage}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Add;
