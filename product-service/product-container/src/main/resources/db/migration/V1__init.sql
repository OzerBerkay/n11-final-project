CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE product.categories (
                                    id uuid NOT NULL,
                                    name character varying COLLATE pg_catalog."default" NOT NULL,
                                    active boolean NOT NULL,
                                    CONSTRAINT categories_pkey PRIMARY KEY (id)
);

CREATE TABLE product.products (
                                  id uuid NOT NULL,
                                  name character varying COLLATE pg_catalog."default" NOT NULL,
                                  description character varying COLLATE pg_catalog."default" NOT NULL,
                                  price numeric(10,2) NOT NULL,
                                  stock_quantity integer NOT NULL,
                                  category_id uuid NOT NULL,
                                  brand character varying COLLATE pg_catalog."default" NOT NULL,
                                  model character varying COLLATE pg_catalog."default" NOT NULL,
                                  color character varying COLLATE pg_catalog."default",
                                  image_url character varying COLLATE pg_catalog."default",
                                  active boolean NOT NULL,
                                  version bigint NOT NULL,
                                  CONSTRAINT products_pkey PRIMARY KEY (id),
                                  CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES product.categories (id)
);

CREATE TABLE product.product_outbox (
                                        id uuid NOT NULL,
                                        saga_id uuid NOT NULL,
                                        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                        processed_at TIMESTAMP WITH TIME ZONE,
                                        type character varying COLLATE pg_catalog."default" NOT NULL,
                                        payload character varying COLLATE pg_catalog."default" NOT NULL,
                                        outbox_status character varying COLLATE pg_catalog."default" NOT NULL,
                                        version integer NOT NULL,
                                        CONSTRAINT product_outbox_pkey PRIMARY KEY (id)
);

-- Idempotency için SagaId ve Type bazında UNIQUE index
CREATE UNIQUE INDEX "product_outbox_saga_id_type_idx" ON product.product_outbox ("saga_id", "type");