import { SignupForm } from "~/components/Signup"

export default function SignUp() {
  return (
    <main className="bg-black">
      <div className="w-4/5 min-h-screen py-20 m-auto flex justify-center items-center">
        <SignupForm />
      </div>
    </main>
  );
}