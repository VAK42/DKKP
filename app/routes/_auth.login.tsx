import { LoginForm } from "~/components/Login"

export default function Login() {
  return (
    <main className="bg-black">
      <div className="w-4/5 min-h-screen py-20 m-auto flex justify-center items-center">
        <LoginForm />
      </div>
    </main>
  );
}