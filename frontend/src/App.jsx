import { BrowserRouter, Routes, Route, Link } from "react-router-dom"
import Login from './pages/Login.jsx'
import Home from './pages/Home.jsx'
import Category from './pages/Category.jsx'
import Setting from './pages/Settings.jsx'
import NotFound from './pages/NotFound.jsx'
import Layout from './components/Layout.jsx'
import AuthRequired from './components/AuthRequired.jsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/login" element={<Login />}/>
        </Route>

        <Route element={<AuthRequired/>}>
          <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="/home" element={<Home />}/>
            <Route path="/category/:id" element={<Category />}/>
            <Route path="/setting" element={<Setting />}/>
            <Route path="*" element={<NotFound />} />
          </Route>
        </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;
