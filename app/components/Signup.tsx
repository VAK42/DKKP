import { cn } from "~/utils/shadcn";
import { Button } from "~/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "~/components/ui/card";
import { Input } from "~/components/ui/input";
import { Label } from "~/components/ui/label";
import { Form, Link } from "@remix-run/react";

export function SignupForm({
  className,
  ...props
}: React.ComponentPropsWithoutRef<"div">) {
  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card className="border-4 border-purple-500">
        <CardHeader className="flex justify-center items-center">
          <CardTitle className="text-2xl text-purple-500">SIGN UP</CardTitle>
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
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="abc"
                  required
                />
              </div>
              <Button type="submit" className="w-full bg-purple-500">
                Sign Up
              </Button>
            </div>
            <div className="mt-4 text-center text-sm">
              Have An Account?
              <Link to="/login" className="ml-12 underline underline-offset-4">
                Log In
              </Link>
            </div>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}