import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import search from "./search";
import contact from "./contact";
import add from "./add";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Router>
    <Switch>
      <Route exact path="/" component={search} />
      <Route exact path="/contact/:id" component={contact} />
      <Route exact path="/add" component={add} />
    </Switch>
  </Router>
);
