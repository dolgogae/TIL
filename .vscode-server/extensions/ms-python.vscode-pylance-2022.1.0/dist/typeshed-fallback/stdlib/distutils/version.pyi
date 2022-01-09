from abc import abstractmethod
from typing import Pattern, TypeVar

_T = TypeVar("_T", bound=Version)

class Version:
    def __repr__(self) -> str: ...
    def __eq__(self, other: object) -> bool: ...
    def __lt__(self: _T, other: _T | str) -> bool: ...
    def __le__(self: _T, other: _T | str) -> bool: ...
    def __gt__(self: _T, other: _T | str) -> bool: ...
    def __ge__(self: _T, other: _T | str) -> bool: ...
    @abstractmethod
    def __init__(self, vstring: str | None = ...) -> None: ...
    @abstractmethod
    def parse(self: _T, vstring: str) -> _T: ...
    @abstractmethod
    def __str__(self) -> str: ...
    @abstractmethod
    def _cmp(self: _T, other: _T | str) -> bool: ...

class StrictVersion(Version):
    version_re: Pattern[str]
    version: tuple[int, int, int]
    prerelease: tuple[str, int] | None
    def __init__(self, vstring: str | None = ...) -> None: ...
    def parse(self: _T, vstring: str) -> _T: ...
    def __str__(self) -> str: ...
    def _cmp(self: _T, other: _T | str) -> bool: ...

class LooseVersion(Version):
    component_re: Pattern[str]
    vstring: str
    version: tuple[str | int, ...]
    def __init__(self, vstring: str | None = ...) -> None: ...
    def parse(self: _T, vstring: str) -> _T: ...
    def __str__(self) -> str: ...
    def _cmp(self: _T, other: _T | str) -> bool: ...
