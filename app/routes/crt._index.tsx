import { ActionFunction } from "@remix-run/node";
import { Form } from "@remix-run/react";
import axios from "axios";
import { useEffect, useState } from "react";

type Product = {
  name: string;
  price: number;
  quantity: number;
  total: number;
};

export const action: ActionFunction = async ({ request }) => {
  try {
    const formData = new URLSearchParams(await request.text());
    const cart: Product[] = JSON.parse(formData.get('cart') || '[]');
    const response = await axios.post("http://localhost:8081/dkkp/cart.php", { products: cart });
    const data = response.data;
    return new Response(JSON.stringify(data), {
      headers: { "Content-Type": "application/json" },
    });
  } catch (error) {
    return new Response(
      JSON.stringify({ error: "Failed To Upload Products!" }),
      { status: 500, headers: { "Content-Type": "application/json" } }
    );
  }
};

export default function Cart() {
  const [cartItems, setCartItems] = useState<Product[]>([]);
  const [message, setMessage] = useState<string>("");
  const [success, setSuccess] = useState<boolean>(false);
  useEffect(() => {
    const storedCartItems = JSON.parse(sessionStorage.getItem("cart") || "[]");
    const updatedCartItems = [...storedCartItems];
    for (let i = 0; i < updatedCartItems.length; i++) {
      updatedCartItems[i].total = updatedCartItems[i].price * updatedCartItems[i].quantity;
    }
    sessionStorage.setItem("cart", JSON.stringify(updatedCartItems));
    setCartItems(updatedCartItems);
  }, []);
  const handleIncreaseQuantity = (index: number) => {
    const updatedCartItems = [...cartItems];
    updatedCartItems[index].quantity++;
    updatedCartItems[index].total =
      updatedCartItems[index].price * updatedCartItems[index].quantity;
    setCartItems(updatedCartItems);
    sessionStorage.setItem("cart", JSON.stringify(updatedCartItems));
  };
  const handleDecreaseQuantity = (index: number) => {
    const updatedCartItems = [...cartItems];
    if (updatedCartItems[index].quantity > 1) {
      updatedCartItems[index].quantity--;
      updatedCartItems[index].total =
        updatedCartItems[index].price * updatedCartItems[index].quantity;
      setCartItems(updatedCartItems);
      sessionStorage.setItem("cart", JSON.stringify(updatedCartItems));
    }
  };
  const handleDeleteItem = (index: number) => {
    const updatedCartItems = cartItems.filter((_, i) => i !== index);
    setCartItems(updatedCartItems);
    sessionStorage.setItem("cart", JSON.stringify(updatedCartItems));
  };
  const handleCheckout = async () => {
    try {
      setMessage("Thank You For Purchasing!");
      setSuccess(true);
      setCartItems([]);
      sessionStorage.removeItem("cart");
    } catch (error) {
      setMessage("Something Went Wrong!");
      setSuccess(false);
    }
  };
  useEffect(() => {
    if (message !== "") {
      const timer = setTimeout(() => {
        setMessage("");
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [message]);
  return (
    <main className="text-white bg-black pt-20 relative">
      {message !== "" && (
        <div
          className={`text-purple-500 border-2 border-purple-500 p-2 m-4 right-0 absolute ${success ? "bg-green-50" : "bg-red-50"}`}
        >
          {message}
        </div>
      )}
      <div className="w-4/5 min-h-screen bg-slate-950 border-2 border-purple-500 rounded-2xl mt-8 mx-auto">
        <div className="text-4xl before:content-[''] before:grow before:m-auto before:border-b-2 before:border-purple-500 after:content-[''] after:grow after:m-auto after:border-b-2 after:border-purple-500 flex justify-center items-center">
          <i className="fa-solid fa-cart-shopping text-8xl p-4"></i>
        </div>
        {cartItems.length > 0 ? (
          <div className="flex justify-center items-center">
            <table
              className={
                "w-11/12 text-purple-500 bg-white border border-purple-500 border-collapse my-4"
              }
            >
              <thead>
                <tr className={"flex bg-green-100"}>
                  <td
                    className={
                      "border border-purple-500 w-[5%] flex justify-center items-center"
                    }
                  ></td>
                  <td
                    className={
                      "border border-purple-500 w-[65%] flex justify-center items-center"
                    }
                  >
                    Products
                  </td>
                  <td
                    className={
                      "border border-purple-500 w-[10%] flex justify-center items-center"
                    }
                  >
                    Quantity
                  </td>
                  <td
                    className={
                      "border border-purple-500 w-[10%] flex justify-center items-center"
                    }
                  >
                    Price
                  </td>
                  <td
                    className={
                      "border border-purple-500 w-[10%] flex justify-center items-center"
                    }
                  >
                    Total
                  </td>
                </tr>
              </thead>
              {cartItems.map((crt, index) => (
                <tbody key={crt.name}>
                  <tr className="flex">
                    <td className="border border-purple-500 w-[5%] flex justify-center items-center">
                      <button
                        className={"w-full h-full hover:bg-red-200"}
                        onClick={() => handleDeleteItem(index)}
                      >
                        <i className="fa-regular fa-trash"></i>
                      </button>
                    </td>
                    <td className="border border-purple-500 w-[65%] px-4">
                      {crt.name}
                    </td>
                    <td className="border border-purple-500 w-[10%] flex justify-center items-center">
                      <button
                        className="w-1/5 hover:bg-red-200"
                        onClick={() => handleDecreaseQuantity(index)}
                      >-</button>
                      <div className="w-3/5 border-x-2 border-purple-500 text-center">
                        {crt.quantity}
                      </div>
                      <button
                        className="w-1/5 hover:bg-green-200"
                        onClick={() => handleIncreaseQuantity(index)}
                      >+</button>
                    </td>
                    <td
                      className="border border-purple-500 w-[10%] flex justify-center items-center"
                    >
                      {crt.price}
                    </td>
                    <td
                      className="border border-purple-500 w-[10%] flex justify-center items-center"
                    >
                      {crt.total}
                    </td>
                  </tr>
                </tbody>
              ))}
            </table>
          </div>
        ) : (
          <div className="flex justify-center items-center">
            <i className="fa-light fa-face-sad-sweat text-2xl mx-2"></i>
            <p className="text-2xl">There Is Nothing In Your Current Cart!</p>
            <i className="fa-light fa-face-sad-sweat text-2xl mx-2"></i>
          </div>
        )}
        {cartItems.length > 0 ? (
          <Form method="POST" action="/action" className="flex justify-center items-center">
            <input type="hidden" name="cart" value={JSON.stringify(cartItems)} />
            <button
              type="submit"
              className={
                "text-purple-500 bg-white border-2 border-purple-500 rounded-2xl px-2 mx-2 float-right"
              }
              onClick={handleCheckout}
            >
              CHECKOUT
            </button>
          </Form>
        ) : (
          ""
        )}
      </div>
    </main>
  )
}