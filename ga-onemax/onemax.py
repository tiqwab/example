from random import randint, random

class Gene:
    def __init__(self, allele):
        assert isinstance(allele, int)
        self.allele = allele

    def mutate(self, mutator):
        return Gene(mutator.randomize(self.allele))

    def copy(self):
        return Gene(self.allele)

    def __str__(self):
        return "%s" % str(self.allele)

    def __repr__(self):
        return "%s" % str(self.allele)

class Chromosome:
    def __init__(self, genes):
        assert isinstance(genes, list)
        self.genes = genes

    @classmethod
    def generate(self, mutator, size):
        genes = [Gene(mutator.gen_random()) for x in range(0, size)]
        return Chromosome(genes)

    def mutate(self, mutator):
        genes = [x.mutate(mutator) for x in self.genes]
        return Chromosome(genes)

    def crossover(self, other):
        genes1 = [x.copy() for x in self.genes]
        genes2 = [x.copy() for x in other.genes]
        point = randint(1, len(genes1)-2)
        chrom1 = Chromosome(genes1[:point] + genes2[point:])
        chrom2 = Chromosome(genes2[:point] + genes1[point:])
        return (chrom1, chrom2)

    def evaluate(self, evaluator):
        return evaluator(self)

    def copy(self):
        genes = [x.copy() for x in self.genes]
        return Chromosome(genes)

    def __str__(self):
        return "Chromosome {genes=%s}" % str(self.genes)

    def __repr__(self):
        return "Chromosome {gene=%s}" % str(self.genes)

class Phenotype:
    def __init__(self, chrom, fitness):
        self.chrom = chrom
        self.fitness = fitness

    def __str__(self):
        return "Phenotype {chrom=%s, fitness=%s}" % (self.chrom, self.fitness)

class Population:
    def __init__(self, pop_size, gene_size, mutator, evaluator, selector, incubator):
        self.pop_size = pop_size
        self.gene_size = gene_size
        self.mutator = mutator
        self.evaluator = evaluator
        self.selector = selector
        self.incubator = incubator

    def evaluate(self, chroms):
        '''
        Evaluate chromosomes and return phenotypes.
        '''
        return list(map(lambda x: Phenotype(x, x.evaluate(self.evaluator)), chroms))

    def select(self, pts):
        '''
        Select elites and others, return their phenotypes.
        '''
        return self.selector(pts)

    def mate(self, pts):
        '''
        Mate phenotypes, return chromosomes.
        '''
        return self.incubator.incubate(pts)

    def mutate(self, chroms):
        '''
        Return chromosomes applied mutation.
        '''
        return [x.mutate(self.mutator) for x in chroms]

    def cycle(self, chroms):
        '''
        Generate new chromosomes from the given ones through the selection.
        '''
        # evaluate
        generation = self.evaluate(chroms)
        print(self.take_stat(generation))
        # select
        selected, elites = self.select(generation)
        # mate
        mated = self.mate(selected)
        # mutate
        mutated = self.mutate(mated)
        return mutated + [x.chrom for x in elites]

    def evolve(self, count):
        '''
        Perform evolution by repeating cycles 'count' times.
        '''
        chroms  = [Chromosome.generate(self.mutator, self.gene_size) \
                for x in range(0, self.pop_size)]
        for i in range(0, count):
           chroms = self.cycle(chroms)
        return chroms

    def take_stat(self, generation):
        '''
        Return statistics (Min, Max, Avg of fitnesses) in the generation.
        '''
        sorted_gen = sorted(generation, key=lambda x: x.fitness)
        average_fit = sum([x.fitness for x in generation]) / len(generation)
        return "Min: %s -> %d, Max: %s -> %d, Avg: %f" % \
                (sorted_gen[0].chrom, sorted_gen[0].fitness, \
                sorted_gen[-1].chrom, sorted_gen[-1].fitness, \
                average_fit)

class Mutator:
    '''
    Class to mutate genes.
    '''
    def __init__(self, randomizer, prob):
        self.randomizer = randomizer
        self.prob = prob

    def gen_random(self):
        return self.randomizer()

    def randomize(self, original):
        if random() < self.prob:
            return self.gen_random()
        else:
            return original

def onemax_randomizer():
    '''
    Generate random allele.
    '''
    return randint(0, 1)

def onemax_evaluator(chrom):
    '''
    Evaluate chromosome.
    '''
    return len(list(filter(lambda x: x == 1, map(lambda x: x.allele, chrom.genes))))

def onemax_selector(pts):
    '''
    Return selected phenotypes and elites from the givin phenotypes.
    Select only top one as elite.
    '''
    elite = max(pts, key=lambda x: x.fitness)

    fit_sum = sum([x.fitness for x in pts])
    gen_size = len(pts)
    next_pts = []
    for i in range(0, gen_size):
        rand = randint(0, fit_sum)
        acc_sum = 0
        for j in range(0, gen_size):
            acc_sum += pts[j].fitness
            if acc_sum >= rand:
                next_pts.append(pts[j])
                break

    return next_pts, [elite]

class Incubator:
    '''
    Class to perform cross-over.
    '''
    def __init__(self, mater, prob):
        self.mater = mater
        self.prob = prob

    def incubate(self, pts):
        return self.mater(pts, self.prob)

def onemax_mater(pts, prob):
    '''
    Perform cross-over between two chromosomes in the givin phenotypes.
    '''
    chroms = []
    group_size = len(pts)
    for i in range(0, int(group_size / 2)):
        pt1 = pts[2*i]
        pt2 = pts[2*i+1]
        if random() < prob:
            chrom1, chrom2 = pt1.chrom.crossover(pt2.chrom)
        else:
            chrom1, chrom2 = pt1.chrom, pt2.chrom
        chroms.append(chrom1)
        chroms.append(chrom2)
    if group_size % 2 != 0:
        chroms.append(pts[-1].chrom)
    return chroms

if __name__ == '__main__':
    pop = Population(10, 20, Mutator(onemax_randomizer, 0.01), \
            onemax_evaluator, onemax_selector, Incubator(onemax_mater, 0.8))
    pop.evolve(100)
