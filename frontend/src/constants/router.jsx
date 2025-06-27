import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from "react-router-dom";
import App from '../App.jsx'
import Landing from '../components/Landing.jsx'
import Home from "../components/Home.jsx";
import SignUp from "../components/Signup.jsx";
import Login from "../components/Login.jsx";

export const router = createBrowserRouter(
  createRoutesFromElements(
    <Route element={<App />}>
      <Route index element={<Landing />} />
      <Route path="home" index element={<Home />} />
      <Route path="signup" element={<SignUp />} />
      <Route path="login" element={<Login />} />
    </Route>
  )
)
