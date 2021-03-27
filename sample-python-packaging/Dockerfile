FROM python:3.9 as builder

RUN curl -sSL -O https://raw.githubusercontent.com/python-poetry/poetry/master/get-poetry.py && \
    python get-poetry.py

ENV PATH "/root/.poetry/bin:$PATH"

WORKDIR /work

COPY . .

RUN poetry build



FROM python:3.9

ARG package_version

COPY --from=builder /work/dist /dist

RUN pip install /dist/example-$package_version-py3-none-any.whl

CMD ["hello"]
