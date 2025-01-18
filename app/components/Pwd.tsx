import { cn } from "~/utils/shadcn";
import { Button } from "~/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "~/components/ui/card";
import { Input } from "~/components/ui/input";
import { Label } from "~/components/ui/label";
import { Form } from "@remix-run/react";
import { useState } from "react";

export function Pwd({
  className,
  ...props
}: React.ComponentPropsWithoutRef<"div">) {
  const [btn, setBtn] = useState<boolean>(false);
  const [success, setSuccess] = useState<boolean>(false);
  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card className="border-4 border-purple-500">
        <CardHeader className="flex justify-center items-center">
          <CardTitle className="text-2xl text-purple-500">RESET PASSWORD</CardTitle>
        </CardHeader>
        <CardContent>
          <Form>
            {!success ? (
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
                {!btn ? (
                  <Button type="submit" className="w-full bg-purple-500" onClick={() => setBtn(true)}>
                    SEND CODE
                  </Button>
                ) : (
                  <div className="grid gap-2">
                    <div className="grid gap-2">
                      <Label htmlFor="Token">Token</Label>
                      <Input
                        id="token"
                        type="text"
                        placeholder="abc"
                        required
                      />
                    </div>
                    <Button type="submit" className="w-full bg-purple-500" onClick={() => setSuccess(true)}>
                      SUBMIT
                    </Button>
                  </div>
                )}
              </div>
            ) : (
              <span className="flex justify-center items-center">Reset Successfully!</span>
            )}
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}