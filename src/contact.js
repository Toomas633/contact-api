import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "./contact.css";

function Contact() {
  const [result, setResult] = useState(null);
  const { id } = useParams();
  const [popupMessage, setPopupMessage] = useState("");
  const [Nimi, setInput1] = useState("");
  const [Salajane, setInput2] = useState("");
  const [Tel, setInput3] = useState("");

  useEffect(() => {
    let data = {
      value: id,
    };
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8000/get");
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          const response = JSON.parse(xhr.responseText);
          setInput1(response.nimi);
          setInput2(response.salajane);
          setInput3(response.tel);
          setResult(response);
        } else {
          console.error("Error:", xhr.status);
        }
      }
    };
    xhr.send(JSON.stringify(data));
  }, [id]);

  const handleBackClick = () => {
    window.location.href = "/";
  };

  const handleDeleteClick = () => {
    const data = {
      id: id,
    };
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8000/delete");
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.send(JSON.stringify(data));

    xhr.onreadystatechange = () => {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          setPopupMessage("Kontakt kustutatud");
          setTimeout(function () {
            window.location.href = "/";
          }, 1000);
        } else {
          const errorMessage = xhr.responseText || "Error";
          setPopupMessage(errorMessage);
        }
      }
    };
  };

  const handleSaveClick = () => {
    if (!(Nimi < 1 || Salajane < 1 || Tel < 1)) {
      const data = {
        id: id,
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
            setPopupMessage("Kontakt muudetud");
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

  const handleInputChange1 = (event) => {
    setInput1(event.target.value);
  };

  const handleInputChange2 = (event) => {
    setInput2(event.target.value);
  };

  const handleInputChange3 = (event) => {
    setInput3(event.target.value);
  };

  return (
    <div className="Contact">
      <div className="navbar">
        <button className="back" onClick={handleBackClick}>
          Tagasi
        </button>
      </div>
      <div className="data">
        {result ? (
          <div className="result">
            <p>Nimi</p>
            <input type="text" value={Nimi} onChange={handleInputChange1} />
            <p>Koodnimi</p>
            <input type="text" value={Salajane} onChange={handleInputChange2} />
            <p>Tel</p>
            <input type="text" value={Tel} onChange={handleInputChange3} />
            <br></br>
            <br></br>
            <button className="savebtn" onClick={handleSaveClick}>
              Salvesta
            </button>
            <button className="deletebtn" onClick={handleDeleteClick}>
              Kustuta
            </button>
          </div>
        ) : (
          <p>Loading...</p>
        )}
        {popupMessage && (
          <div
            className={`popup ${
              popupMessage === "Kontakt muudetud" || "Kontakt kustutatud"
                ? "success"
                : "error"
            }`}
          >
            {popupMessage}
          </div>
        )}
      </div>
    </div>
  );
}

export default Contact;
