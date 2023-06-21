--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8 (Ubuntu 14.8-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.8 (Ubuntu 14.8-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: kontaktid; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kontaktid (
    id integer NOT NULL,
    nimi text,
    salajane text,
    tel text
);


ALTER TABLE public.kontaktid OWNER TO postgres;

--
-- Name: kasutajad_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kasutajad_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kasutajad_id_seq OWNER TO postgres;

--
-- Name: kasutajad_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kasutajad_id_seq OWNED BY public.kontaktid.id;


--
-- Name: kontaktid id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kontaktid ALTER COLUMN id SET DEFAULT nextval('public.kasutajad_id_seq'::regclass);


--
-- Data for Name: kontaktid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kontaktid (id, nimi, salajane, tel) FROM stdin;
1	Mart Laanemäe	Kivimurdja	55512345
2	Liis Saar	Metslind	+372 55567890
3	Jaanika Tamm	Salurüütel	+372 55524680
4	Peeter Vaher	Öökull	55510101
5	Mari-Liis Kask	Sinitihane	55588899
6	Märten Põldmäe	Virmalaine	+37255598765
\.


--
-- Name: kasutajad_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kasutajad_id_seq', 4, true);


--
-- Name: kontaktid kasutajad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kontaktid
    ADD CONSTRAINT kasutajad_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

