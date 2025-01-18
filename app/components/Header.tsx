import { Form, Link } from "@remix-run/react";
import { Input } from "./ui/input";
import { useState } from "react";

interface Product {
  id: string;
  name: string;
}

export default function Header() {
  const [menu, setMenu] = useState<boolean>(false);
  const guest = false;
  const showAlert = () => {
    alert("Please Log In To Continue!")
  };
  const search: Product[] = [];
  const mn = ["phone", "laptop"];
  return (
    <header className="w-full h-20 text-white bg-slate-950 border-b border-white flex fixed z-50">
      <Link to="/" className="w-2/12 flex justify-center items-center">
        <img src="app/IMG/DKKP_00.png" alt="DKKP" className="h-full" />
      </Link>
      <div className="w-1/12">
        <div className="h-full flex justify-center items-center hover:text-purple-500 anmt" onClick={() => setMenu(!menu)}>
          {!menu ? (
            <i className="fa-solid fa-bars text-2xl"></i>
          ) : (
            <i className="fa-solid fa-x text-2xl"></i>
          )}
        </div>
        {menu && (
          <div className="text-purple-500 bg-white border-2 border-purple-500 rounded mt-2">
            {mn.map((mn, index) => (
              <Link key={index} to={mn} className="block py-0.5 m-2 anmt">
                {mn.charAt(0).toUpperCase() + mn.slice(1)}
              </Link>
            ))}
          </div>
        )}
      </div>
      <div className="w-6/12">
        <Form className="h-full flex justify-center items-center">
          <Input className="w-2/3 text-purple-500 bg-white" />
        </Form>
        {search.length > 0 && (
          <div className="w-1/2 text-purple-500 bg-white border-2 border-purple-500 rounded mt-2 mx-auto">
            {search.map((item, index) => (
              <Link key={index} to={item.id} className="block py-0.5 m-2 anmt">
                {item.name}
              </Link>
            ))}
          </div>
        )}
      </div>
      <Link to="tel:19001900" className="w-1/12 flex justify-center items-center hover:text-purple-500 anmt">
        <i className="fa-solid fa-phone text-2xl"></i>
      </Link>
      {!guest ? (
        <Link to="/crt" className="w-1/12 flex justify-center items-center hover:text-purple-500 anmt">
          <i className="fa-solid fa-cart-shopping text-2xl"></i>
        </Link>
      ) : (
        <button onClick={showAlert} className="w-1/12 flex justify-center items-center hover:text-purple-500 anmt">
          <i className="fa-solid fa-cart-shopping text-2xl"></i>
        </button>
      )}
      {!guest ? (
        <Link to="/usr" className="w-1/12 flex justify-center items-center hover:text-purple-500 anmt">
          <i className="fa-solid fa-user text-2xl"></i>
        </Link>
      ) : (
        <Link to="/login" className="w-1/12 flex justify-center items-center hover:text-purple-500 anmt">
          <i className="fa-solid fa-user text-2xl"></i>
        </Link>
      )}
    </header>
  );
}