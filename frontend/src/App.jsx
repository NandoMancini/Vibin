import Navbar from './components/Navbar'
import { Outlet } from "react-router-dom"

// import Header from './components/Header'


export default function App() {
  return (
    <>
      <Navbar />
      <main className="pt-24">
        <Outlet />
      </main>
    </>
  )
}