import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./component/Navbar";
import Dashboard from "./pages/dashboard";
import BasicTable from "./pages/table";
function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route exact path="/" element={<Dashboard/>} />
        <Route path="/table" element={<BasicTable/>} />
      </Routes>
    </Router>
  );
}
export default App;