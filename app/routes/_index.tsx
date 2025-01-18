import { Link } from "@remix-run/react";
import { Carousel } from "../components/Carousel";
import { Slide } from "../components/Slide";
import { useLoaderData } from "@remix-run/react";
import type { LoaderFunction } from "@remix-run/node";
import axios from "axios";

interface Product {
  id: string;
  img: string;
  name: string;
  price: number;
  discount: number;
  disPrice?: number;
}

interface MainDTO {
  hotsales: Product[];
  phone: Product[];
  laptop: Product[];
}

export const loader: LoaderFunction = async () => {
  try {
    const response = await axios.get<MainDTO>("http://localhost:8081/dkkp/index.php");
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

export default function Index() {
  const { hotsales, phone, laptop } = useLoaderData<MainDTO>();
  const img = [
    "app/IMG/DKKP_00.png",
    "app/IMG/DKKP_02.png",
    "app/IMG/DKKP_04.png",
  ];
  hotsales.forEach((item) => {
    item.disPrice = item.price * (1 - item.discount / 100);
  });
  phone.forEach((item) => {
    item.disPrice = item.price * (1 - item.discount / 100);
  });
  laptop.forEach((item) => {
    item.disPrice = item.price * (1 - item.discount / 100);
  });
  return (
    <main className="min-h-screen bg-black">
      <div className="w-4/5 py-20 m-auto">
        <Carousel img={img} />
        <div className="h-[25vw] bg-red-100 border-y-2 border-red-500 rounded mt-16">
          <div className="h-1/5 text-red-900 text-2xl flex justify-center items-center">
            <i className="fa-light fa-fire m-2 text-2xl"></i>HOT SALES
            <i className="fa-light fa-fire m-2 text-2xl"></i>
          </div>
          <div className="h-4/5">
            <Slide products={hotsales} />
          </div>
        </div>
        <div className="h-[25vw] bg-slate-950 border-y-2 border-purple-500 rounded mt-16">
          <div className="h-1/5 flex">
            <div className="w-1/5 h-full text-white text-2xl flex justify-center items-center">
              <i className="fa-light fa-mobile text-2xl m-2"></i>PHONE
            </div>
            <div className="w-4/5 h-full flex justify-end items-center px-20">
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/apple"}
              >
                Apple
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/samsung"}
              >
                Samsung
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/xiaomi"}
              >
                Xiaomi
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/huawei"}
              >
                Huawei
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/oppo"}
              >
                Oppo
              </Link>
            </div>
          </div>
          <div className="h-4/5">
            <Slide products={phone} />
          </div>
        </div>
        <div className="h-[25vw] bg-slate-950 border-y-2 border-purple-500 rounded mt-16">
          <div className="h-1/5 flex">
            <div className="w-1/5 h-full text-white text-2xl flex justify-center items-center">
              <i className="fa-light fa-laptop text-2xl m-2"></i>LAPTOP
            </div>
            <div className="w-4/5 h-full flex justify-end items-center px-20">
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/acer"}
              >
                Acer
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/asus"}
              >
                Asus
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/dell"}
              >
                Dell
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/hp"}
              >
                HP
              </Link>
              <Link
                className="text-purple-900 bg-white border-2 border-purple-500 rounded px-2 mx-2"
                to={"/msi"}
              >
                MSI
              </Link>
            </div>
          </div>
          <div className="h-4/5">
            <Slide products={laptop} />
          </div>
        </div>
      </div>
    </main>
  );
}