import { Pwd } from "~/components/Pwd"

export default function PWD() {
  return (
    <main className="bg-black">
      <div className="w-4/5 min-h-screen py-20 m-auto flex justify-center items-center">
        <Pwd />
      </div>
    </main>
  );
}