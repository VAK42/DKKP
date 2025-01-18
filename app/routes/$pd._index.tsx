import { Form } from "@remix-run/react";
import { useState } from "react";
import { Input } from "../components/ui/input"
import { Button } from "~/components/ui/button";
import { Textarea } from "~/components/ui/textarea";
import { LoaderFunction } from "@remix-run/node";
import axios from "axios";

interface Product {
  id: string;
  img: string;
  name: string;
  description: string;
  optionValue: string;
  attributeName: string;
  atrributeValue: string;
  price: number;
  discount: number;
  disPrice?: number;
}

export const loader: LoaderFunction = async ({ params }) => {
  try {
    const { pd } = params;
    const response = await axios.get<Product>(`http://localhost:8081/dkkp/product.php?id=${pd}`);
    const data: Product = response.data;
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

export default function Product() {
  const { data } = useLoaderData<Product>();
  data.disPrice = data.price * (1 - data.discount / 100);
  const [name, setName] = useState('');
  const [feedback, setFeedback] = useState('');
  return (
    <main className="text-white bg-black pt-20 relative">
      <div className="w-4/5 min-h-screen bg-slate-950 border-2 border-purple-500 rounded-2xl mt-8 mx-auto">
        <div className="text-4xl before:content-[''] before:grow before:border-b-2 before:border-purple-500 after:content-[''] after:grow after:border-b-2 after:border-purple-500 flex justify-center items-center p-4">
          PRODUCT
        </div>
        <div className="h-[25vw] flex">
          <div className="w-1/2 flex justify-center items-center px-4">
            <img src={data.img} alt="" className="w-full h-5/6 border-2 border-purple-500 rounded-2xl" />
          </div>
          <div className="w-1/2 flex justify-center items-center px-4">
            <div className="w-full h-5/6 bg-white border-2 border-purple-500 rounded-2xl">
              <div className="h-1/4 flex justify-center items-center">
                <span className="text-2xl text-purple-500 border-b-2 border-purple-500 p-2">{data.name}</span>
              </div>
              <div className="h-1/2 text-purple-500 flex p-4">
                <button className="h-fit border-2 border-purple-500 rounded-xl p-2 m-2">{data.optionValue}</button>
                <button className="h-fit border-2 border-purple-500 rounded-xl p-2 m-2">{data.optionValue}</button>
                <button className="h-fit border-2 border-purple-500 rounded-xl p-2 m-2">{data.optionValue}</button>
              </div>
              <div className="h-1/4 border-t-2 border-dashed border-purple-500 flex justify-center items-center">
                <div>
                  <span className="text-3xl text-red-500 px-2">{data.disPrice}</span>
                  <span className="line-through text-xl text-black px-2">{data.price}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="min-h-[25vw] flex">
          <div className="w-3/4 flex justify-center items-center px-4">
            <div className="w-full h-5/6 text-xl text-purple-500 bg-white border-2 border-purple-500 rounded-2xl p-4">{data.description}</div>
          </div>
          <div className="w-1/4 flex justify-center items-center px-4">
            <div className="w-full h-5/6 bg-white border-2 border-purple-500 rounded-2xl flex justify-center">
              <table
                className="w-11/12 text-purple-500 bg-white border-0.5 border-purple-500 border-collapse my-4"
              >
                <tbody>
                  <tr className="flex">
                    <td className="border border-purple-500 w-1/2 px-4">{data.attributeName}</td>
                    <td className="border border-purple-500 w-1/2 px-4">{data.atrributeValue}</td>
                  </tr>
                  <tr className="flex">
                    <td className="border border-purple-500 w-1/2 px-4">{data.attributeName}</td>
                    <td className="border border-purple-500 w-1/2 px-4">{data.atrributeValue}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <Form className="border-t-2 border-dashed border-purple-500 px-4 mb-4">
          <div>
            <label htmlFor="name" className="text-xl text-purple-500">
              Name
            </label>
            <Input
              type="text"
              id="name"
              className="w-1/4 text-purple-500 bg-white border-2 border-purple-500 rounded my-2"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div>
            <label htmlFor="feedback" className="text-xl text-purple-500">
              Feedback
            </label>
            <Textarea
              id="feedback"
              className="w-1/4 text-purple-500 bg-white border-2 border-purple-500 rounded my-2"
              value={feedback}
              onChange={(e) => setFeedback(e.target.value)}
              required
            />
          </div>

          <div className="text-center">
            <Button
              type="submit"
              className="px-2 text-purple-500 bg-white border-2 border-purple-500"
            >
              Submit
            </Button>
          </div>
        </Form>
      </div>
    </main>
  )
}