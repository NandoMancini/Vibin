// src/components/SignUp.jsx
import {
  Center,
  Box,
  Button,
  Input,
  Heading,
  Text,
  VStack,
  Link,
} from "@chakra-ui/react";
import { FormControl, FormLabel } from "@chakra-ui/form-control";
import { useState } from "react";

export default function SignUp({ onSwitchToLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Signing up:", { email, password });
  };

  return (
    <Center minH="100vh" px={4}>
      <Box
        as="form"
        onSubmit={handleSubmit}
        w="full"
        maxW={{ base: "90%", md: "400px" }}
        p={{ base: 6, md: 8 }}
        bg="whiteAlpha.150"
        backdropFilter="auto"
        backdropBlur="12px"
        borderRadius="3xl"
        borderWidth="1px"
        borderColor="whiteAlpha.300"
        boxShadow="xl"
        // switch your brand font in your theme, or override here:
        fontFamily="body"
        color="white"
      >
        <VStack spacing={6} align="stretch">
          <Heading
            as="h2"
            size="xl"
            fontWeight="extrabold"
            letterSpacing="tight"
            textAlign="center"
            // or fontFamily="'Pacifico', cursive" for a script-y vibe
          >
            Create Account
          </Heading>

          <FormControl id="email" isRequired>
            <FormLabel
              fontSize="sm"
              fontWeight="medium"
              color="whiteAlpha.800"
            >
              Email address
            </FormLabel>
            <Input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
              bg="whiteAlpha.200"
              color="white"
              _placeholder={{ color: "whiteAlpha.600" }}
              fontWeight="medium"
            />
          </FormControl>

          <FormControl id="password" isRequired>
            <FormLabel
              fontSize="sm"
              fontWeight="medium"
              color="whiteAlpha.800"
            >
              Password
            </FormLabel>
            <Input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              bg="whiteAlpha.200"
              color="white"
              _placeholder={{ color: "whiteAlpha.600" }}
              fontWeight="medium"
            />
          </FormControl>

          <Button
            type="submit"
            colorScheme="teal"
            size="lg"
            fontSize="md"
            fontWeight="bold"
            letterSpacing="wide"
          >
            Sign Up
          </Button>

          <Text textAlign="center" fontSize="sm" color="whiteAlpha.800">
            Already have an account?{" "}
            <Link className="text-yellow-400" fontWeight="medium" onClick={onSwitchToLogin}>
              Log in
            </Link>
          </Text>
        </VStack>
      </Box>
    </Center>
  );
}
