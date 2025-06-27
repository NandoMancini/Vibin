// src/components/Login.jsx
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

export default function Login({ onSwitchToSignUp }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Logging in:", { email, password });
    // TODO: call your login API here
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
          >
            Welcome Back
          </Heading>

          <FormControl id="email" isRequired>
            <FormLabel fontSize="sm" fontWeight="medium" color="whiteAlpha.800">
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
            <FormLabel fontSize="sm" fontWeight="medium" color="whiteAlpha.800">
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
            transition="all 0.2s ease"
           _hover={{
             boxShadow: "lg",
             bg: "teal.600",          // slightly darker on hover
           }}
          >
            Log In
          </Button>

          <Text textAlign="center" fontSize="sm" color="whiteAlpha.800">
            Don’t have an account?{" "}
            <Link
              color="yellow.300"
              fontWeight="medium"
              onClick={onSwitchToSignUp}
            >
              Sign up
            </Link>
          </Text>
        </VStack>
      </Box>
    </Center>
  );
}
