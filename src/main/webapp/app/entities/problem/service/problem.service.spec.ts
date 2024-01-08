import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProblem } from '../problem.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../problem.test-samples';

import { ProblemService } from './problem.service';

const requireRestSample: IProblem = {
  ...sampleWithRequiredData,
};

describe('Problem Service', () => {
  let service: ProblemService;
  let httpMock: HttpTestingController;
  let expectedResult: IProblem | IProblem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProblemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Problem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const problem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(problem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Problem', () => {
      const problem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(problem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Problem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Problem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Problem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProblemToCollectionIfMissing', () => {
      it('should add a Problem to an empty array', () => {
        const problem: IProblem = sampleWithRequiredData;
        expectedResult = service.addProblemToCollectionIfMissing([], problem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(problem);
      });

      it('should not add a Problem to an array that contains it', () => {
        const problem: IProblem = sampleWithRequiredData;
        const problemCollection: IProblem[] = [
          {
            ...problem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProblemToCollectionIfMissing(problemCollection, problem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Problem to an array that doesn't contain it", () => {
        const problem: IProblem = sampleWithRequiredData;
        const problemCollection: IProblem[] = [sampleWithPartialData];
        expectedResult = service.addProblemToCollectionIfMissing(problemCollection, problem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(problem);
      });

      it('should add only unique Problem to an array', () => {
        const problemArray: IProblem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const problemCollection: IProblem[] = [sampleWithRequiredData];
        expectedResult = service.addProblemToCollectionIfMissing(problemCollection, ...problemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const problem: IProblem = sampleWithRequiredData;
        const problem2: IProblem = sampleWithPartialData;
        expectedResult = service.addProblemToCollectionIfMissing([], problem, problem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(problem);
        expect(expectedResult).toContain(problem2);
      });

      it('should accept null and undefined values', () => {
        const problem: IProblem = sampleWithRequiredData;
        expectedResult = service.addProblemToCollectionIfMissing([], null, problem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(problem);
      });

      it('should return initial array if no Problem is added', () => {
        const problemCollection: IProblem[] = [sampleWithRequiredData];
        expectedResult = service.addProblemToCollectionIfMissing(problemCollection, undefined, null);
        expect(expectedResult).toEqual(problemCollection);
      });
    });

    describe('compareProblem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProblem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProblem(entity1, entity2);
        const compareResult2 = service.compareProblem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProblem(entity1, entity2);
        const compareResult2 = service.compareProblem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProblem(entity1, entity2);
        const compareResult2 = service.compareProblem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
