import React from 'react';
const Navbar = () => {
    return (
    <nav className="fixed top-4 left-0 right-0 z-50 flex justify-center">
      {/* Logo / Signature in top-left */}
      <div className="absolute top-4 left-6">
        <a href="#" className="font-signature text-5xl text-accent select-none text-yellow-400">
          Vibin
        </a>
      </div>
    </nav>
  );
}

export default Navbar