import { LoaderFunction } from "@remix-run/node";
import { useLoaderData, useNavigate } from "@remix-run/react";
import axios from "axios";

interface userInfo {
  username: string;
  email: string;
  phone: number;
  address: string;
}

interface productInfo {
  name: string;
  price: number;
  quantity: number;
  total: number;
}

interface MainDTO {
  userInfo: userInfo;
  productInfo: productInfo[];
}

export const loader: LoaderFunction = async () => {
  try {
    const response = await axios.get<MainDTO>("http://localhost:8081/dkkp/user.php");
    const data: MainDTO = response.data;
    return new Response(JSON.stringify(data), {
      headers: { "Content-Type": "application/json" },
    });
  } catch (error) {
    return new Response(
      JSON.stringify({ error: "Failed To Fetch Data!" }),
      { status: 500, headers: { "Content-Type": "application/json" } }
    );
  }
};

export default function Account() {
  const navigate = useNavigate();
  const { userInfo, productInfo } = useLoaderData<MainDTO>();
  const handleLogout = () => {
    navigate('/login');
  };
  return (
    <main className="text-white bg-black pt-20 relative">
      <div className="w-4/5 min-h-screen bg-slate-950 border-2 border-purple-500 rounded-2xl mt-8 mx-auto">
        <div
          className="text-center text-4xl before:content-[''] before:grow before:m-auto before:border-b-2 before:border-purple-500 after:content-[''] after:grow after:m-auto after:border-b-2 after:border-purple-500 flex justify-center items-center"
        >
          <i className="fa-light fa-circle-user text-8xl p-4"></i>
        </div>
        {userInfo && (
          <div className="w-1/2 m-auto">
            <div className="h-[10vw] text-purple-500 bg-white border-2 border-purple-500 p-4">
              <div className="w-full float-left">
                Username:<span className="float-right">{userInfo.username}</span>
              </div>
              <div className="w-full float-left">
                Email:<span className="float-right">{userInfo.email}</span>
              </div>
              <div className="w-full float-left">
                Phone Number:<span className="float-right">{userInfo.phone}</span>
              </div>
              <div className="w-full float-left">
                Address:<span className="float-right">{userInfo.address}</span>
              </div>
            </div>
            <div className="w-full flex justify-center items-center">
              <button
                onClick={handleLogout}
                className="text-purple-500 bg-white border-2 border-purple-500 my-2 px-2"
              >
                Logout
              </button>
            </div>
          </div>
        )}
        <div
          className="text-center text-4xl before:content-[''] before:grow before:m-auto before:border-b-2 before:border-purple-500 after:content-[''] after:grow after:m-auto after:border-b-2 after:border-purple-500 flex justify-center items-center my-10"
        >
          <i className="fa-solid fa-cart-shopping text-8xl p-4"></i>
        </div>
        {productInfo.length > 0 && (
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
                      "border border-purple-500 w-[70%] flex justify-center items-center"
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
              {productInfo.map((crt, index) => (
                <tbody key={index}>
                  <tr className="flex">
                    <td className="border border-purple-500 w-[70%] px-4">
                      {crt.name}
                    </td>
                    <td className="border border-purple-500 w-[10%] flex justify-center items-center">
                      {crt.quantity}
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
          </div>)}
      </div>
    </main>
  );
}