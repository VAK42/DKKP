import { cn } from "~/utils/shadcn";
import { Button } from "~/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "~/components/ui/card";
import { Input } from "~/components/ui/input";
import { Label } from "~/components/ui/label";
import { Form, Link } from "@remix-run/react";

export function LoginForm({
  className,
  ...props
}: React.ComponentPropsWithoutRef<"div">) {
  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card className="border-4 border-purple-500">
        <CardHeader className="flex justify-center items-center">
          <CardTitle className="text-2xl text-purple-500">LOGIN</CardTitle>
        </CardHeader>
        <CardContent>
          <Form>
            <div className="flex flex-col gap-6">
              <div className="grid gap-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="abc@example.com"
                  required
                />
              </div>
              <div className="grid gap-2">
                <div className="flex items-center">
                  <Label htmlFor="password">Password</Label>
                  <Link
                    to="/fgpwd"
                    className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                  >
                    Forgot Password?
                  </Link>
                </div>
                <Input
                  id="password"
                  type="password"
                  placeholder="abc"
                  required
                />
              </div>
              <Button type="submit" className="w-full bg-purple-500">
                Login
              </Button>
            </div>
            <div className="mt-4 text-center text-sm">
              Don&apos;t Have An Account?
              <Link to="/signup" className="ml-4 underline underline-offset-4">
                Sign Up
              </Link>
            </div>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}